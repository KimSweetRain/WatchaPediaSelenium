package com.watcha.seleniumtest.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "tbBoxoffice")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoxOffice {
    @Id
    private Long boxRangking;
    @OneToOne
    @JoinColumn(name = "box_mov_idx")
    private Movie movie;
    private String boxMovTitle;
    private double boxBooking;
    private Long boxSpectators;
}
