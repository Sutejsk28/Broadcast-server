package com.sutej.broadcast.modals;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subcastId;

    @NotBlank(message = "subcast name is required")
    private String subcastName;
//
//    @NotBlank(message = "Description of the subcast is required")
    private String subcastDescription;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Post> posts;

    private Instant createdDate;

}
