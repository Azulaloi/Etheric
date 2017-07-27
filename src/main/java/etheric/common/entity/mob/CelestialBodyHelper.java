package etheric.common.entity.mob;

import net.minecraft.entity.EntityBodyHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;

/**
 * Created by Azulaloi on 7/19/2017
 */
public class CelestialBodyHelper extends EntityBodyHelper {
    //EntityBodyHelper but with an extra condition for if the mob is pathing
    //If you pass it something that isn't an EntityCelestial or subclass thereof, it won't work
    //That could be changed, but doesn't need to right now.

    private EntityLivingBase living;

    private int rotationTickCounter;
    private float prevRenderYawHead;

    public CelestialBodyHelper(EntityLivingBase livingIn){
        super(livingIn);
        this.living = livingIn;
    }

    public void updateRenderAngles() {
        double d0 = this.living.posX - this.living.prevPosX;
        double d1 = this.living.posZ - this.living.prevPosZ;

        if (d0 * d0 + d1 * d1 > 2.500000277905201E-7D) {
            this.living.renderYawOffset = this.living.rotationYaw;
            this.living.rotationYawHead = this.computeAngleWithBound(this.living.renderYawOffset, this.living.rotationYawHead, 75.0F);
            this.prevRenderYawHead = this.living.rotationYawHead;
            this.rotationTickCounter = 0;
        } else {
            if (this.living.getPassengers().isEmpty() || !(this.living.getPassengers().get(0) instanceof EntityLiving)) {
                float f = 75.0F;
                if (this.living instanceof EntityCelestial) { //Just casting...
                    if (((EntityCelestial) this.living).getMoveHelper().isUpdating()) { //IF YOU ARE MOOOOVING
                        if (Math.abs(this.living.rotationYawHead - this.prevRenderYawHead) > 80.0F) { //FACE THAT DIRECTION
                            this.rotationTickCounter = 0;
                            this.prevRenderYawHead = this.living.rotationYawHead;
                        }
                    } else if (Math.abs(this.living.rotationYawHead - this.prevRenderYawHead) > 15.0F) {
                        this.rotationTickCounter = 0;
                        this.prevRenderYawHead = this.living.rotationYawHead;
                    } else {
                        ++this.rotationTickCounter;
                        int i = 10;

                        if (this.rotationTickCounter > 10) {
                            f = Math.max(1.0F - (float) (this.rotationTickCounter - 10) / 10.0F, 0.0F) * 75.0F;
                        }
                    }

                    this.living.renderYawOffset = this.computeAngleWithBound(this.living.rotationYawHead, this.living.renderYawOffset, f);
                }
            }
        }
    }

    /**
     * Return the new angle2 such that the difference between angle1 and angle2 is lower than angleMax. Args : angle1,
     * angle2, angleMax
     */
    public float computeAngleWithBound ( float p_75665_1_, float p_75665_2_, float p_75665_3_){
        float f = MathHelper.wrapDegrees(p_75665_1_ - p_75665_2_);

        if (f < -p_75665_3_) {
            f = -p_75665_3_;
        }

        if (f >= p_75665_3_) {
            f = p_75665_3_;
        }

        return p_75665_1_ - f;
    }
}
