package com.diskfood.diskfoodapi.core.jackson;

import com.diskfood.diskfoodapi.api.model.mixin.CidadeMixin;
import com.diskfood.diskfoodapi.api.model.mixin.CozinhaMixin;
import com.diskfood.diskfoodapi.api.model.mixin.RestauranteMixin;
import com.diskfood.diskfoodapi.domain.model.Cidade;
import com.diskfood.diskfoodapi.domain.model.Cozinha;
import com.diskfood.diskfoodapi.domain.model.Restaurante;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Component;

@Component
public class JacksonMixinModule extends SimpleModule {

    public JacksonMixinModule() {
        setMixInAnnotation(Restaurante.class, RestauranteMixin.class);
        setMixInAnnotation(Cidade.class, CidadeMixin.class);
        setMixInAnnotation(Cozinha.class, CozinhaMixin.class);
    }

}
