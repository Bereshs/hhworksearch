package ru.bereshs.hhworksearch.hhapiclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class HhListDto<T> {
    private int found;
    private List<T> items;
    private int page;
    private int pages;
    @JsonProperty("per_page")
    private int perPage;

    public HhListDto (List<T> list) {
        setItems(list);
    }

    public List<T> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }
}
