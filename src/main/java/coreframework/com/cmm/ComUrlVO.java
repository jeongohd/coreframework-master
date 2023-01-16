package coreframework.com.cmm;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

//url
@SuppressWarnings("serial")
@Getter
@Setter
public class ComUrlVO implements Serializable {
    /** redirect url */
    private String rdrUrl = "";
    
    /** forward url */
    private String fwdUrl = "";

    /** self url**/
    private String selfUrl = "";
    

}
