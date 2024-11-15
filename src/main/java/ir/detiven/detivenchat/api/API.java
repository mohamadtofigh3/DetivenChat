package ir.detiven.detivenchat.api;

/**
 * 
 *   by: Detiven
 *   
 *   In: 10/5/2024
 * 
 */
public interface API {
    
    /**
     * + Default:
     * detectWithFont: true
     * clearSpammedChar: true
     * replacer: true
     * 
     * @param str is bad word or contains bad word, return is true.
     * @return boolean value.
     */
    boolean isSwear(String str);

    /**
     * + Default:
     * clearSpammedChar: true
     * replacer: true
     * 
     * @param str is bad word or contains bad word, return is true.
     * @param detectWithFont if need to detect with font, use true. (if disable in config. this is not working!)
     * @return boolean value.
     */
    boolean isSwear(String str, boolean detectWithFont);

    /**
     * + Default:
     * replacer: true
     * 
     * @param str is bad word or contains bad word, return is true.
     * @param detectWithFont if need to detect with font, use true. (if disable in config.yml, this is not working!)
     * @param clearSpammedChar clear spammed char. (selectable in config.yml)
     * @return boolean value.
     */
    boolean isSwear(String str, boolean detectWithFont, boolean clearSpammedChar);

    /**
     * @param str is bad word or contains bad word, return is true.
     * @param detectWithFont if need to detect with font, use true. (if disable in config.yml, this is not working!)
     * @param clearSpammedChar clear spammed char. (selectable in config.yml)
     * @param replacer replacing character selected in config.yml (for anti bypass!)
     * @return boolean value.
     */
    boolean isSwear(String str, boolean detectWithFont, boolean clearSpammedChar, boolean replacer);
    
}