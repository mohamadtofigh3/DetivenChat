package ir.detiven.detivenchat.api;

import ir.detiven.detivenchat.modules.antiswear.objects.SwearObject;

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
     * detectWithFont: false
     * clearSpammedChar: false
     * replacer: false
     * clearCharacter: false
     * 
     * @param str is bad word or contains bad word, return is true.
     * @return SwearObject.
     */
    SwearObject isSwear(String str);

    /**
     * + Default:
     * clearSpammedChar: false
     * replacer: false
     * clearCharacter: false
     * 
     * @param str is bad word or contains bad word, return is true.
     * @param detectWithFont if need to detect with font, use true. (if disable in config. this is not working!)
     * @return SwearObject.
     */
    SwearObject isSwear(String str, boolean detectWithFont);

    /**
     * + Default:
     * replacer: false
     * clearCharacter: false
     * 
     * @param str is bad word or contains bad word, return is true.
     * @param detectWithFont if need to detect with font, use true. (if disable in config.yml, this is not working!)
     * @param clearSpammedChar clear spammed char. (selectable in config.yml)
     * @return SwearObject.
     */
    SwearObject isSwear(String str, boolean detectWithFont, boolean clearSpammedChar);

    /**
     * + Default:
     * clearCharacter: false
     *
     * @param str is bad word or contains bad word, return is true.
     * @param detectWithFont if need to detect with font, use true. (if disable in config.yml, this is not working!)
     * @param clearSpammedChar clear spammed char. (selectable in config.yml)
     * @param replacer replacing character selected in config.yml (for anti bypass!)
     * @return SwearObject.
     */
    SwearObject isSwear(String str, boolean detectWithFont, boolean clearSpammedChar, boolean replacer);

    /**
     * @param str is bad word or contains bad word, return is true.
     * @param detectWithFont if need to detect with font, use true. (if disable in config.yml, this is not working!)
     * @param clearSpammedChar clear spammed char. (selectable in config.yml)
     * @param replacer replacing character selected in config.yml (for anti bypass!)
     * @param clearCharacter filter character, in config.yml (for anti bypass!)
     * @return SwearObject.
     */
    SwearObject isSwear(String str, boolean detectWithFont, boolean clearSpammedChar, boolean replacer, boolean clearCharacter);
    
}
