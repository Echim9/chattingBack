package com.example.chattingback.eneity;

import com.example.chattingback.enums.Rcode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Data
public class Response {

    private Rcode code;

    private String msg;

    private Object data;

    Response() {
    }

    Response(builder builder) {
        this.code = builder.code;
        if (!StringUtils.isEmpty(builder.msg)) {
            this.msg = builder.msg;
        }
        if (!ObjectUtils.isEmpty(builder.data)) {
            this.data = builder.data;
        }
    }

    public static builder builder() {
        return new builder();
    }

    public static class builder {

        Rcode code;

        String msg;

        Object data;

        public builder code(Rcode code) {
            this.code = code;
            return this;
        }

        public builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public builder data(Object data) {
            this.data = data;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}
