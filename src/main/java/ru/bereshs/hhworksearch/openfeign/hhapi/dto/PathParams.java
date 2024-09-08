package ru.bereshs.hhworksearch.openfeign.hhapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PathParams {
    private Integer page;
    private Integer per_page;
    private String text;
    @JsonProperty("search_field")
    private String searchFiled;
    private Integer period;
    private Integer host;

    public String toString() {
        StringBuilder result = new StringBuilder();
        if (page != null) result.append("page=").append(page).append("&");
        if (per_page != null) result.append("per_page=").append(per_page).append("&");
        if (text != null) result.append("text=").append(text).append("&");
        if (searchFiled != null) result.append("search_field=").append(searchFiled).append("&");
        if (period != null) result.append("period=").append(period).append("&");
        if (host != null) result.append("host=").append(host).append("&");


        return result.toString();
    }
}

