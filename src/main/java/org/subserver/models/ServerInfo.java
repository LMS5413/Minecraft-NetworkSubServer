package org.subserver.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class ServerInfo {
    private int PID;
    private String name;
    private String logs = "";
}
