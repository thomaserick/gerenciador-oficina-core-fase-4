package com.fiap.pj.core.email.domain;

import com.fiap.pj.core.email.domain.enums.Template;

import java.util.List;

public record EmailMessage(String destinatario,
                           Template template,
                           List<Object> placeholders) {
}
