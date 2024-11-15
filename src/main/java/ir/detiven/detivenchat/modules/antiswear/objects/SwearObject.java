package ir.detiven.detivenchat.modules.antiswear.objects;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@Getter
@RequiredArgsConstructor
public class SwearObject {
    
    private final boolean swear;
    
    private final String word;

}
