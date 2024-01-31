package com.diskfood.diskfoodapi.api.exceptionhandler;

public enum ProblemType {
    ENTIDADE_EM_USO("Entidade em uso", "/entidade-em-uso"),
    ERRO_NEGOCIO("Violação de regra de negócio", "/erro-negocio"),
    CORPO_REQUISICAO_INVALIDO("corpo da chamada invalido", "/corpo-invalido"),
    PARAMETRO_INVALIDO("parametro de URL inválido", "/parametro-invalido"),
    ERRO_DE_SISTEMA("Erro de Sistema", "/erro-sistema"),
    DADOS_INVALIDOS("Dados Inválidos", "/dados-invalidos"),
    RECURSO_NAO_ENCONTRADO("Recurso não encontrado", "/recuros-nao-encontrado");

    private String title;
    private String uri;

    ProblemType(String title, String path) {
        this.title = title;
        this.uri = "https://diskfood.com.br" + path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
