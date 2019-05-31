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
package org.wso2.carbon.esb.mediator.test.rewrite;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wso2.esb.integration.common.utils.ESBIntegrationTest;

import javax.xml.xpath.XPathExpressionException;

import static org.testng.Assert.assertTrue;

public class AppendPathTestCase extends ESBIntegrationTest {
    @BeforeClass(alwaysRun = true) public void uploadSynapseConfig() throws Exception {
        super.init();
        verifyProxyServiceExistence("urlRewriteAppendPathTestProxy");
    }

    @Test(groups = {
            "wso2.esb" }, description = "Append text to path", dataProvider = "addressingUrl") public void appendPath(
            String addUrl) throws AxisFault, XPathExpressionException {
        OMElement response;

        //log.info("appendPath : " + getProxyServiceURLHttp("urlRewriteProxy"));
        log.info("getBackEndServiceUrl : " + getBackEndServiceUrl(""));
        log.info("addUrl : " + addUrl);
        response = axis2Client
                .sendSimpleStockQuoteRequest(getProxyServiceURLHttp("urlRewriteAppendPathTestProxy"), addUrl, "IBM");
        assertTrue(response.toString().contains("IBM"));

    }

    @AfterClass(alwaysRun = true) private void destroy() throws Exception {
        super.cleanup();
    }

    @DataProvider(name = "addressingUrl") public Object[][] addressingUrl() throws XPathExpressionException {
        return new Object[][] { { getBackEndServiceUrl("").substring(0, (getBackEndServiceUrl("").length() - 1)) },
                { getBackEndServiceUrl("").substring(0, (getBackEndServiceUrl("").length() - 1)).replace("http",
                        "https") }, };

    }

}
