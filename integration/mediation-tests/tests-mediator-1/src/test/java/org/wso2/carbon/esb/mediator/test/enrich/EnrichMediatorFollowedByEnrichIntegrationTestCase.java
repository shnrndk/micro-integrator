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
import org.wso2.esb.integration.common.clients.registry.ResourceAdminServiceClient;
import org.wso2.esb.integration.common.utils.ESBIntegrationTest;
import org.wso2.esb.integration.common.utils.ESBTestConstant;

import java.net.URL;
import javax.activation.DataHandler;
import javax.xml.namespace.QName;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class EnrichMediatorFollowedByEnrichIntegrationTestCase extends ESBIntegrationTest {
    private ResourceAdminServiceClient resourceAdminServiceStub;

    @BeforeClass(alwaysRun = true) public void uploadSynapseConfig() throws Exception {
        super.init();
        resourceAdminServiceStub = new ResourceAdminServiceClient(contextUrls.getBackEndUrl(),
                context.getContextTenant().getContextUser().getUserName(),
                context.getContextTenant().getContextUser().getPassword());
        uploadResourcesToGovernanceRegistry();
        verifyProxyServiceExistence("enrichTwiceTestProxy");
    }

    @Test(groups = {
            "wso2.esb" }, description = "Enrich mediator followed by enrich mediator") public void enrichMediatorFollowedByEnrichMediator()
            throws Exception {
        OMElement response = axis2Client.sendCustomQuoteRequest(getProxyServiceURLHttp("enrichTwiceTestProxy"),
                getBackEndServiceUrl(ESBTestConstant.SIMPLE_STOCK_QUOTE_SERVICE), "IBM");
        assertNotNull(response, "Response message is null");
        assertEquals(response.getLocalName(), "CheckPriceResponse", "CheckPriceResponse not match");
        assertTrue(response.toString().contains("Price"), "No price tag in response");
        assertTrue(response.toString().contains("Code"), "No code tag in response");
        assertEquals(response.getFirstChildWithName(new QName("http://services.samples/xsd", "Code")).getText(), "MSFT",
                "Symbol not changed");

    }

    private void uploadResourcesToGovernanceRegistry() throws Exception {

        resourceAdminServiceStub.deleteResource("/_system/governance/xslt");
        resourceAdminServiceStub.addCollection("/_system/governance/", "xslt", "", "Contains test XSLT files");
        resourceAdminServiceStub
                .addResource("/_system/governance/xslt/transform_back.xslt", "application/xml", "xslt files",
                        new DataHandler(new URL("file:///" + getESBResourceLocation()
                                + "/mediatorconfig/xslt/transform_back.xslt")));

    }

    @AfterClass(alwaysRun = true) private void destroy() throws Exception {

        resourceAdminServiceStub.deleteResource("/_system/governance/xslt");
        cleanup();
    }
}
