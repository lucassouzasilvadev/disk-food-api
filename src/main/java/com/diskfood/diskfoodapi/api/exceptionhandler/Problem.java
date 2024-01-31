package com.diskfood.diskfoodapi.api.exceptionhandler;

import java.time.LocalDateTime;
import java.util.List;

public class Problem {
    private Integer status;
    private LocalDateTime timestamp;
    private String type;
    private String title;
    private String detail;
    private String userMessage;
    private List<Object> objects;

    private Problem() {
        // Construtor privado
    }



    public static class Object {
        private String name;
        private String userMessage;

        public Object() {
            // Construtor padrão
        }

        public static class FieldBuilder {
            private String name;
            private String userMessage;

            public FieldBuilder name(String name) {
                this.name = name;
                return this;
            }

            public FieldBuilder userMessage(String userMessage) {
                this.userMessage = userMessage;
                return this;
            }

            public Object build() {
                Object object = new Object();
                object.name = this.name;
                object.userMessage = this.userMessage;
                return object;
            }
        }

        public static FieldBuilder builder() {
            return new FieldBuilder();
        }

        // Getters e setters para name e userMessage
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserMessage() {
            return userMessage;
        }

        public void setUserMessage(String userMessage) {
            this.userMessage = userMessage;
        }
    }

    // Getters para todos os campos da classe Problem

    public Integer getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getUserMessage() {
        return userMessage;
    }


    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }


    // Classe Builder
    public static class ProblemBuilder {
        private Integer status;
        private LocalDateTime timestamp;
        private String type;
        private String title;
        private String detail;
        private String userMessage;
        private List<Object> objects;

        public ProblemBuilder status(Integer status) {
            this.status = status;
            return this;
        }

        public ProblemBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ProblemBuilder type(String type) {
            this.type = type;
            return this;
        }

        public ProblemBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ProblemBuilder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public ProblemBuilder userMessage(String userMessage) {
            this.userMessage = userMessage;
            return this;
        }

        public ProblemBuilder objects(List<Object> objects) {
            this.objects = objects;
            return this;
        }

        public Problem build() {
            Problem problem = new Problem();
            problem.status = this.status;
            problem.timestamp = this.timestamp;
            problem.type = this.type;
            problem.title = this.title;
            problem.detail = this.detail;
            problem.userMessage = this.userMessage;
            problem.objects = this.objects;
            return problem;
        }
    }

    public static ProblemBuilder builder() {
        return new ProblemBuilder();
    }
}