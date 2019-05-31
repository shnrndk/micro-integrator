/*
 *Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *WSO2 Inc. licenses this file to you under the Apache License,
 *Version 2.0 (the "License"); you may not use this file except
 *in compliance with the License.
 *You may obtain a copy of the License at
 *
 *http://www.apache.org/licenses/LICENSE-2.0
 *
 *Unless required by applicable law or agreed to in writing,
 *software distributed under the License is distributed on an
 *"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *KIND, either express or implied.  See the License for the
 *specific language governing permissions and limitations
 *under the License.
 */
package org.wso2.carbon.esb.mediator.test.enrich;

import org.apache.axiom.om.OMElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.esb.integration.common.utils.ESBIntegrationTest;

import javax.xml.namespace.QName;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class EnrichIntegrationBodyToXpathSiblingTestCase extends ESBIntegrationTest {
    @BeforeClass(alwaysRun = true) public void setEnvironment() throws Exception {
        super.init();
        verifyProxyServiceExistence("enrichBodyToXpathSiblingTestProxy");
    }

    //get the body of response and add it as a sibling of xpath ://ns:getQuote/ns:request
    @Test(groups = "wso2.esb", description = "Add custom content as a sibling to the part of message"
            + " specified by xpath expression ") public void testEnrichMediator() throws Exception {
        OMElement response;

        response = axis2Client
                .sendSimpleStockQuoteRequest(getProxyServiceURLHttp("enrichBodyToXpathSiblingTestProxy"), null, "WSO2");

        assertNotNull(response, "Response message null");

        OMElement returnTag = response.getFirstChildWithName(new QName("http://services.samples", "return"));

        OMElement bodySibling = (OMElement) returnTag.getNextOMSibling();
        OMElement returnTagInsideBodySibling = bodySibling
                .getFirstChildWithName(new QName("http://services.samples", "return"));
        OMElement symbolTag = returnTagInsideBodySibling
                .getFirstChildWithName(new QName("http://services.samples/xsd", "symbol"));
        assertNotNull(bodySibling, "Fault sibling not added.");
        assertEquals(bodySibling.getLocalName(), "getQuoteResponse", "Fault, sibling ");
        assertEquals(symbolTag.getText(), "WSO2", "Fault, sibling");

    }

    @AfterClass(alwaysRun = true) public void close() throws Exception {
        super.cleanup();
    }

}
