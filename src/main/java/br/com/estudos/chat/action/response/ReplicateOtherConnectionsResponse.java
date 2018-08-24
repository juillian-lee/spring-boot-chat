package br.com.estudos.chat.action.response;

public class ReplicateOtherConnectionsResponse {
    private Response response;

    public ReplicateOtherConnectionsResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
