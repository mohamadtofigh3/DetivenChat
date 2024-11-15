package ir.detiven.detivenchat.modules.connection.objects;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@Getter
@RequiredArgsConstructor
public class ConnectionObject {

    private final ConnectionAction action;

    private final String permission;

    private final String message;
    
}