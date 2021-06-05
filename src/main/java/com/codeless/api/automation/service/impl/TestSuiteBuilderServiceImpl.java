package com.codeless.api.automation.service.impl;

import com.codeless.api.automation.domain.Header;
import com.codeless.api.automation.domain.Test;
import com.codeless.api.automation.domain.Validator;
import com.codeless.api.automation.domain.ValidatorAttribute;
import com.codeless.api.automation.service.TestSuiteBuilderService;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class TestSuiteBuilderServiceImpl implements TestSuiteBuilderService {

  @Override
  public String build(List<Test> tests) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      Document document = docBuilder.newDocument();

      Element suiteNode = createSuite(document);
      document.appendChild(suiteNode);

      for (Test test : tests) {
        Element testNode = createTest(document, test);
        suiteNode.appendChild(testNode);

        List<Header> headers = test.getHeaders();
        if (headers != null) {
          addHeaders(headers, document, testNode);
        }

        List<Validator> validators = test.getValidators();
        if (validators != null) {
          addValidators(validators, document, testNode);
        }
      }
      return toString(document);

    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  private void addHeaders(List<Header> headers, Document document, Element testNode) {
    for (Header header : headers) {
      Element headerNode = createHeader(document, header);
      testNode.appendChild(headerNode);
    }
  }

  private void addValidators(List<Validator> validators, Document document, Element testNode) {
    Map<String, List<Validator>> validatorsByName = getValidatorsByName(validators);
    for (Entry<String, List<Validator>> validator : validatorsByName.entrySet()) {
      Element validatorNode = createValidator(document, validator.getKey());
      testNode.appendChild(validatorNode);

      for (Validator rule : validator.getValue()) {
        Element ruleNode = createRule(document, rule);
        validatorNode.appendChild(ruleNode);
      }
    }
  }

  private Element createHeader(Document document, Header header) {
    Element headerNode = document.createElement("header");
    headerNode.setAttribute("name", header.getName());
    headerNode.setAttribute("value", header.getValue());
    return headerNode;
  }

  private Map<String, List<Validator>> getValidatorsByName(List<Validator> validators) {
    Map<String, List<Validator>> validatorsByName = new HashMap<>();
    for (Validator validator : validators) {
      String validatorName = validator.getDslName();
      List<Validator> validatorRules = validatorsByName
          .getOrDefault(validatorName, new ArrayList<>());
      validatorRules.add(validator);
      validatorsByName.put(validatorName, validatorRules);
    }
    return validatorsByName;
  }

  private Element createSuite(Document document) {
    Element suiteNode = document.createElement("suite");
    suiteNode.setAttribute("name", "codeless");
    return suiteNode;
  }

  private Element createTest(Document document, Test test) {
    Element testNode = document.createElement("test");
    testNode.setAttribute("name", test.getName());
    testNode.setAttribute("url", test.getRequestURL());
    testNode.setAttribute("httpMethod", test.getHttpMethod());
    return testNode;
  }

  private Element createValidator(Document document, String validatorName) {
    return document.createElement(validatorName);
  }

  private Element createRule(Document document, Validator validatorRule) {
    Element rule = document.createElement("rule");
    if (validatorRule.getPredicate() != null) {
      rule.setAttribute("name", validatorRule.getPredicate());
    }
    List<ValidatorAttribute> attributes = validatorRule.getInputFields();
    if (attributes != null) {
      for (ValidatorAttribute attribute : attributes) {
        rule.setAttribute(attribute.getDslName(), attribute.getValue());
      }
    }
    return rule;
  }

  private String toString(Document document) {
    try {
      StringWriter sw = new StringWriter();
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer transformer = tf.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.METHOD, "xml");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.transform(new DOMSource(document), new StreamResult(sw));
      return sw.toString();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
