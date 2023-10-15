package mom.beaver.first.helpers;

import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class soundHelper {

    public static SoundEvent pickRandomSoundEvent(SoundEvent[] soundArray) {
        int soundIndex = (int)(Math.random() * soundArray.length);
        return soundArray[soundIndex];
    }
}
