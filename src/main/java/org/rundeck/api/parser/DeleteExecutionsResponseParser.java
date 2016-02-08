package org.rundeck.api.parser;

import org.dom4j.Node;
import org.rundeck.api.domain.DeleteExecutionsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * DeleteExecutionsResponseParser is ...
 *
 * @author Greg Schueler <greg@simplifyops.com>
 * @since 2014-11-06
 */
public class DeleteExecutionsResponseParser extends BaseXpathParser<DeleteExecutionsResponse> {

    public DeleteExecutionsResponseParser(final String xpath) {
        super(xpath);
    }

    @Override public DeleteExecutionsResponse parse(final Node baseNode) {

        final DeleteExecutionsResponse response = new DeleteExecutionsResponse();
        response.setAllsuccessful(Boolean.parseBoolean(baseNode.valueOf("@allsuccessful")));
        response.setRequestCount(Integer.parseInt(baseNode.valueOf("@requestCount")));
        response.setSuccessCount(Integer.parseInt(baseNode.valueOf("successful/@count")));

        final Node failedNode = baseNode.selectSingleNode("failed");
        //parse failures
        final List<DeleteExecutionsResponse.DeleteFailure> failures = new ArrayList
                <DeleteExecutionsResponse.DeleteFailure>();
        int failedCount = 0;
        if (null != failedNode) {
            failedCount = Integer.parseInt(baseNode.valueOf("failed/@count"));
            final List list = baseNode.selectNodes("failed/execution");

            for (final Object o : list) {
                final Node execNode = (Node) o;
                final DeleteExecutionsResponse.DeleteFailure deleteFailure =
                        new DeleteExecutionsResponse.DeleteFailure();
                deleteFailure.setExecutionId(Long.parseLong(execNode.valueOf("@id")));
                deleteFailure.setMessage(execNode.valueOf("@message"));
                failures.add(deleteFailure);
            }
        }
        response.setFailedCount(failedCount);
        response.setFailures(failures);
        return response;
    }
}
