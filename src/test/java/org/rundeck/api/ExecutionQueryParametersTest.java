package org.rundeck.api;

import org.junit.Assert;
import org.junit.Test;
import org.rundeck.api.query.ExecutionQuery;

import java.util.Arrays;
import java.util.Date;

/**
 * ExecutionQueryParametersTest is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-11-07
 */
public class ExecutionQueryParametersTest {

    @Test
    public void stringParameter() {
        ExecutionQuery.Builder description = ExecutionQuery.builder().description("a description");
        ExecutionQueryParameters executionQueryParameters = new ExecutionQueryParameters(
                description.build()
        );
        ApiPathBuilder param = new ApiPathBuilder("").param(executionQueryParameters);
        Assert.assertEquals("?descFilter=a+description", param.toString());
    }
    @Test
    public void listParameter() {
        ExecutionQuery.Builder description = ExecutionQuery.builder().excludeJobList(
                Arrays.asList(
                        "a",
                        "b"
                )
        );
        ExecutionQueryParameters executionQueryParameters = new ExecutionQueryParameters(
                description.build()
        );
        ApiPathBuilder param = new ApiPathBuilder("").param(executionQueryParameters);
        Assert.assertEquals("?excludeJobListFilter=a&excludeJobListFilter=b", param.toString());
    }
    @Test
    public void dateParameter() {
        ExecutionQuery.Builder description = ExecutionQuery.builder().end(
                new Date(1347581178168L)
        );
        ExecutionQueryParameters executionQueryParameters = new ExecutionQueryParameters(
                description.build()
        );
        ApiPathBuilder param = new ApiPathBuilder("").param(executionQueryParameters);
        //nb: timezone should be GMT
        //2012-09-14T00:06:18Z
        Assert.assertEquals("?end=2012-09-14T00%3A06%3A18Z", param.toString());
    }
}
