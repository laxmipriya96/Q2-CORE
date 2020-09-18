/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vm.qsmart2core.config;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.parser.CustomModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.XMLParser;
import ca.uhn.hl7v2.validation.impl.NoValidation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vm.qsmart2.utils.HL7MsgTypes;
import com.vm.qsmart2.utils.Hl7MsgTrigger;
import com.vm.qsmart2core.hl7.HL7Field;
import com.vm.qsmart2core.hl7.HL7MapStore;
import com.vm.qsmart2core.hl7.HL7Message;
import com.vm.qsmart2core.hl7.HL7Segment;
import com.vm.qsmart2core.hl7.HL7v2Vocabulary;
import com.vm.qsmart2core.repositary.Hl7CdrRepositary;
import com.vm.qsmart2core.repositary.JdbcTemplateRepositary;
import com.vm.qsmart2core.repositary.PatientRepositary;
import com.vm.qsmart2core.service.Hl7MsgService;
import com.vm.qsmart2core.utilities.RootInfo;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.exception.ExceptionUtils;
/**
 *
 * @author Phani
 */
@Configuration
@EnableJms
public class Hl7MsgConsumer {

    private static final Logger logger = LogManager.getLogger(Hl7MsgConsumer.class);

    public static String header = "[HL7_MSG_CONSUMER] ";

    @Autowired
    ObjectMapper mapper;

    @Autowired
    Hl7CdrRepositary hl7Repositary;

    @Autowired
    PatientRepositary patientRepo;

    @Autowired
    JdbcTemplateRepositary jdbcTemplate;

    @Value("${hl7.producer.queue.name:Q2_HL7_PROCESS}")
    private String processQName;

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${app.debug.required:false}")
    private boolean isLogEnabled;

    private Hl7MsgService hl7MsgService;

//    public Hl7MsgConsumer() {
//        hl7MsgService = 
//    }
    @PostConstruct
    public void intializeMsgService() {
        hl7MsgService = new Hl7MsgService(jdbcTemplate);
    }

    private String getSegmentName(String path) {
        return path.substring(0, path.indexOf("."));
    }

    private String getFieldName(String path) {
        int index = ordinalIndexOf(path, ".", 2, false);
        index = index < 1 ? path.length() : index;
        return path.substring(0, index);
    }

    private int ordinalIndexOf(final CharSequence str, final CharSequence searchStr, final int ordinal, final boolean lastIndex) {
        if (str == null || searchStr == null || ordinal <= 0) {
            return -1;
        }
        if (searchStr.length() == 0) {
            return lastIndex ? str.length() : 0;
        }
        int found = 0;
        int index = lastIndex ? str.length() : -1;
        do {
            if (lastIndex) {
                index = str.toString().lastIndexOf(searchStr.toString(), index - searchStr.length());
            } else {
                index = str.toString().indexOf(searchStr.toString(), index + searchStr.length());
            }
            if (index < 0) {
                return index;
            }
            found++;
        } while (found < ordinal);
        return index;
    }

    private String getValueByTagName(HL7MapStore<String, HL7MapStore> store, String path) {
        try {
            if (isLogEnabled) {
                logger.info("{}>>SegementPath:{}", header, path);
            }
            String segmentName = getSegmentName(path);
            List<HL7MapStore> hL7MapStore = store.get(segmentName);
            for (HL7MapStore hL7MapStore1 : hL7MapStore) {
                String fieldPath = getFieldName(path);
                //System.out.println("==>" + fieldPath);
                if (hL7MapStore1.containsKey(fieldPath)) {
                    //System.out.println("==>" + hL7MapStore1.get(fieldPath));
                }
                List<Map> listMaps = hL7MapStore1.get(fieldPath);
                //System.out.println("List map size ==>" + listMaps.size());
                if (listMaps.size() == 1) {
                    //System.out.println("==>" + listMaps.get(0).get(path));
                    return listMaps.get(0).get(path).toString();
                } else {
                    //TODO multiful sub fileds
                    //System.out.println("==>" + listMaps.get(0).get(path));
                    return listMaps.get(0).get(path).toString();
                }
            }
            return "";

        } catch (Exception e) {
            logger.error("Excep:getValueByTagName:Error:{}", path);
            return "";
        }
    }

    @JmsListener(destination = "${hl7.consumer.queue.name:Q2_HL7_IN}")
    public void hl7ReceiveMessage(final Message message) throws JMSException {
        HL7Message hl7Msg = null;
        HL7MapStore<String, HL7MapStore> store = null;
        String fileType = null;
        File file = null;
        RootInfo obj = null;
        try {
            ActiveMQTextMessage messageText = (ActiveMQTextMessage) message;
            if (isLogEnabled) {
                logger.info("{}Enter:onMessage:Msg:[{}]", header, messageText.getText());
            }
            logger.info("{}Enter:onMessage:ReceivedDataSize:[{}]", header, messageText.getText().length());
            hl7Msg = processHL7File(messageText.getText());
            if (hl7Msg != null) {
                store = processMapStore(hl7Msg);
                fileType = getValueByTagName(store, "MSH.9.1") + "-" + getValueByTagName(store, "MSH.9.2");
                // fileType = hl7Msg.getName();
                file = ResourceUtils.getFile("config/hl7/" + fileType + ".json");
                if (isLogEnabled) {
                    logger.info("{}fileType:[{}],FileFound:[{}],file:[{}]", header, fileType, file.exists(), file);
                }
                obj = mapper.readValue(file, RootInfo.class);
                String tableName = obj.getTableName();
                long id = hl7MsgService.readDataFromHl7JSONString("[" + fileType + "] ", obj, store);
                if (id > 0) {
                    String mrnNo = hl7Repositary.findMrnByHl7CdrId(id);
                    long patientId = 0;
                    if (fileType.equalsIgnoreCase("SIU-S12")) {
                        Long pId = patientRepo.findPatientIdByMrn(mrnNo);
                        patientId = pId != null ? pId : 0;
                        if (patientId > 0) {
                            if (isLogEnabled) {
                                logger.info("{}Patient:Exist:ID:[{}]", header, patientId);
                            }
                        } else {
                            obj = null;
                            file = null;
                            file = ResourceUtils.getFile("config/hl7/patient-info.json");
                            obj = mapper.readValue(file, RootInfo.class);
                            patientId = hl7MsgService.readDataFromHl7JSONString("[PATIENT-INFO]", obj, store);
                        }
                    }
                    //long hl7CdrId, long patientId, HL7FileTypes hl7MsgType, String tableName
                    Hl7MsgTrigger fileTriger = new Hl7MsgTrigger(id, patientId, HL7MsgTypes.getInstce(fileType), tableName);
                    jmsTemplate.convertAndSend(processQName, mapper.writeValueAsString(fileTriger));
                    logger.info("{}<<:TriggerNotificaton:[{}]", header, fileTriger.toString());
                } else {
                    logger.info("{}<<unabletoParse:SomethingWentWrong:Hl7DataLength:[{}]", header, messageText.getText().length());
                }
            } else {
                logger.info("{}<<unabletoParseHl7DataLength:[{}]", header, messageText.getText().length());
            }
        } catch (Exception e) {
            logger.error("{}Excep:hl7ReceiveMessage:Error:{}", header, ExceptionUtils.getRootCauseMessage(e));
        } finally {
            hl7Msg = null;
            fileType = null;
            file = null;
        }
    }

    private void extractFileds(HL7Field field, Map<String, String> mapFileds) {
        field.getFields().forEach((subField) -> {
            if (subField.isChild()) {
                extractFileds(subField, mapFileds);
            } else {
                mapFileds.put(subField.getName(), subField.getValue());
            }
        });
    }

    private HL7MapStore<String, HL7MapStore> processMapStore(HL7Message hl7Message) {

        try {
            HL7MapStore<String, HL7MapStore> finalStore = new HL7MapStore();

            if (hl7Message.isChild()) {
                for (HL7Segment segment : hl7Message.getSegments()) {
                    //System.out.println("=====================>" + segment.getName());
                    HL7MapStore<String, Map> mapStore = new HL7MapStore();
                    if (segment.isChild()) {
                        segment.getFields().forEach((field) -> {
                            Map<String, String> mapFileds = new HashMap<>();
                            if (field.isChild()) {
                                extractFileds(field, mapFileds);
                            } else {
                                mapFileds.put(field.getName(), field.getValue());
                            }
                            mapStore.put(field.getName(), mapFileds);
                        });
                    }
                    finalStore.put(segment.getName(), mapStore);
                }
            }
            return finalStore;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private String getJsonString(HL7Message hl7Message) {
        ObjectMapper mapper;
        try {
            mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;
            return mapper.writeValueAsString(hl7Message);
        } catch (Exception e) {
            e.printStackTrace();
            mapper = null;
            return null;
        }
    }

    private HL7Message processHL7File(String hl7Data) {
        try {
//            Hl7InputStreamMessageIterator iter = new Hl7InputStreamMessageIterator(new ByteArrayInputStream(hl7Data.getBytes()));
//            ca.uhn.hl7v2.model.GenericMessage hapiMsg;
//            while (iter.hasNext()) {
//                hapiMsg = iter.next();
//                XMLParser xmlParser = new DefaultXMLParser();
//                String xml = xmlParser.encode(hapiMsg);
//                return processXML(xml, hapiMsg.getVersion(), hapiMsg.getName().replace('_', '-'));
//            }

            HapiContext context = new DefaultHapiContext();
            Parser ourPipeParser = context.getPipeParser();
            ModelClassFactory ourCustomModelClassFactory = new CustomModelClassFactory("com.vm.qsmart2core.model");
            context.setModelClassFactory(ourCustomModelClassFactory);

            ourPipeParser.setValidationContext(new NoValidation());
//            SIU_Z01 zdtA01Message = (SIU_Z01) ourPipeParser.parse(hl7Data);
//            System.out.println("---------------->"+zdtA01Message.getSCH());

            ca.uhn.hl7v2.model.Message hapiMsg = ourPipeParser.parse(hl7Data);
            XMLParser xmlParser = context.getXMLParser();
            String xml = xmlParser.encode(hapiMsg);
            return processXML(xml, hapiMsg.getVersion(), hapiMsg.getName().replace('_', '-'));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HL7Message processXML(String xml, String version, String type) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            HL7v2Vocabulary vocabulary = new HL7v2Vocabulary(version, "HL7V2");
            if (isLogEnabled) {
                logger.info(type + "(" + version + ")" + " (" + vocabulary.getDescription(type) + ")");
            }
            List<Node> totalSegments = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    String presentNodeName = node.getNodeName().trim();
                    if (isLogEnabled) {
                        logger.info("{}Segment:[{}]", header, presentNodeName);
                    }
                    List<Node> currentNodes;
                    if (presentNodeName.indexOf(".") > 1) {
                        currentNodes = getNodeChildNodeByNode((Element) node);
                        for (Node currentNode : currentNodes) {
                            List<Node> currentNodes2;
                            if (isLogEnabled) {
                                logger.info("  ==>" + currentNode.getNodeName().trim());
                            }
                            if (currentNode.getNodeName().trim().indexOf(".") > 1) {
                                currentNodes2 = getNodeChildNodeByNode((Element) currentNode);
                                for (Node node1 : currentNodes2) {
                                    totalSegments.add(node1);
                                }
                            } else {
                                totalSegments.add(currentNode);
                            }
                        }
                    } else {
                        totalSegments.add(node);
                    }
                }
            }

            HL7Message hl7Message = new HL7Message();
            hl7Message.setName(type);
            hl7Message.setDescription(vocabulary.getDescription(type));
            hl7Message.setVersion(version);

            for (Node segment : totalSegments) {
                if (isLogEnabled) {
                    logger.info("+" + segment.getNodeName());
                }
                //  if (segment.getNodeName().equalsIgnoreCase("NTE")) {
                HL7Segment hl7segment = new HL7Segment();
                hl7segment.setName(segment.getNodeName());
                hl7segment.setDescription(vocabulary.getDescription(segment.getNodeName()));

                processStructure((Element) segment, vocabulary, hl7segment);
                hl7Message.addSegment(hl7segment);
                //  }
            }
            return hl7Message;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void processStructure(Element node, HL7v2Vocabulary vocabulary, HL7Segment hl7segment) {
        NodeList childNodes = node.getChildNodes();

        for (int i = 1; i <= childNodes.getLength(); i++) {
            Node cNode = childNodes.item(i);
            if (cNode instanceof Element) {

                NodeList nodeList = cNode.getChildNodes();
                String head = " |--";
                String subHead = " |  |--";
                String subSubHead = " |  |  |--";
                if (nodeList.getLength() > 1) {
                    if (isLogEnabled) {
                        logger.info(head + cNode.getNodeName() + " - " + vocabulary.getDescription(cNode.getNodeName()));
                    }
                    HL7Field hl7ield = new HL7Field();
                    hl7ield.setName(cNode.getNodeName());
                    hl7ield.setDescription(vocabulary.getDescription(cNode.getNodeName()));
                    hl7segment.addSegment(hl7ield);
                    List<Node> currentNodes = getNodeChildNodeByNode((Element) cNode);
                    for (Node currentNode : currentNodes) {
                        List<Node> currentNodes1 = getNodeChildNodeByNode((Element) currentNode);
                        if (currentNodes1.isEmpty()) {
                            if (!currentNode.getNodeName().equalsIgnoreCase("escape")) {
                                String nodeName = currentNode.getNodeName();
                                String temp = nodeName.indexOf(".") > 0 ? nodeName.substring(nodeName.indexOf(".")) : nodeName;
//                            String tagName = cNode.getNodeName() + nodeName.substring(nodeName.indexOf("."));
                                String tagName = cNode.getNodeName() + temp;
                                String content = currentNode.getLastChild().getTextContent().trim();
                                if (isLogEnabled) {
                                    logger.info(subHead + tagName + " - " + vocabulary.getDescription(nodeName) + " - " + content);
                                }
                                HL7Field subField = new HL7Field();
                                subField.setName(tagName);
                                subField.setDescription(vocabulary.getDescription(nodeName));
                                subField.setValue(content);
                                hl7ield.addField(subField);
                            }
                        } else {
                            String currentNodeName = currentNode.getNodeName();
                            String childNodeName = currentNodes1.get(0).getNodeName();
                            String name = currentNodeName + childNodeName.substring(childNodeName.indexOf("."));

                            String subSubFiledName = cNode.getNodeName() + currentNodeName.substring(currentNodeName.indexOf("."));
                            if (isLogEnabled) {
                                logger.info(subHead + subSubFiledName + " - " + vocabulary.getDescription(subSubFiledName));
                            }

                            HL7Field subField = new HL7Field();
                            subField.setName(subSubFiledName);
                            subField.setDescription(vocabulary.getDescription(subSubFiledName));

                            String content = currentNodes1.get(0).getTextContent().trim();
                            String tagName = cNode.getNodeName() + name.substring(name.indexOf("."));
                            if (isLogEnabled) {
                                logger.info(subSubHead + tagName + " - " + vocabulary.getDescription(childNodeName) + " - " + content);
                            }
                            HL7Field subField1 = new HL7Field();
                            subField1.setName(tagName);
                            subField1.setValue(content);

                            subField1.setDescription(vocabulary.getDescription(childNodeName));
                            subField.addField(subField1);

                            hl7ield.addField(subField);

                        }
                    }
                } else {
                    String nodeName = cNode.getNodeName();
                    String content = cNode.getLastChild().getTextContent().trim();
                    if (isLogEnabled) {
                        logger.info(" |--" + nodeName + " - " + vocabulary.getDescription(nodeName) + " - " + content);
                    }
                    HL7Field hl7ield = new HL7Field();
                    hl7ield.setName(nodeName);
                    hl7ield.setDescription(vocabulary.getDescription(nodeName));
                    hl7ield.setValue(content);
                    hl7segment.addSegment(hl7ield);
                }
            } else {
                //System.out.println("else ==>"+cNode.getNodeValue()); 
            }
        }
    }

    private List<Node> getNodeChildNodeByNode(Element node) {
        List<Node> nodes = new ArrayList<>();
        NodeList nodeList1 = node.getChildNodes();
        for (int k = 0; k < nodeList1.getLength(); k++) {

            Node currentNode = nodeList1.item(k);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                nodes.add(currentNode);
            }
        }
        return nodes;
    }
}
