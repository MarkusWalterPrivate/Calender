package de.walter.Calender.task;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreationDTO {
    @NotNull
    private long start;
    @NotNull
    private long end;
    private List<Long> assigneeIds;

    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String image;
}
