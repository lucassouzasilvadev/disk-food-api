package com.diskfood.diskfoodapi.api.model.mixin;

import com.diskfood.diskfoodapi.domain.model.Restaurante;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

public abstract class CozinhaMixin {
    @JsonIgnore
    private List<Restaurante> restaurantes;


}
