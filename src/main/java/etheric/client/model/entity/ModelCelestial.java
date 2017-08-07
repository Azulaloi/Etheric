package etheric.client.model.entity;

import etheric.Etheric;
import etheric.common.entity.mob.EntityCelestial;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelCelestial extends ModelBase {

	public ModelRenderer armL;
	public ModelRenderer legL;
	public ModelRenderer head;
	public ModelRenderer upperTorso;
	public ModelRenderer armR;
	public ModelRenderer legR;
	public ModelRenderer hat;
	public ModelRenderer lowertorso;
	public ModelRenderer lowerArmL;
	public ModelRenderer lowerArmR;

	public ModelCelestial() {
		this.textureWidth = 64;
		this.textureHeight = 64;

		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, -24.0F, 0.0F);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
		this.upperTorso = new ModelRenderer(this, 0, 44);
		this.upperTorso.setRotationPoint(0.0F, -24.0F, -1.0F);
		this.upperTorso.addBox(-6.0F, 0.0F, -2.0F, 12, 12, 6);
		this.lowertorso = new ModelRenderer(this, 16, 16);
		this.lowertorso.setRotationPoint(0.0F, -12.0F, 0.0F);
		this.lowertorso.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);

		this.armL = new ModelRenderer(this, 48, 16);
		this.armL.setRotationPoint(-7.0F, -22.0F, 0.0F);
		this.armL.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);

		this.lowerArmL = new ModelRenderer(this, 48, 28);
		this.lowerArmL.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.lowerArmL.addBox(0.0F, 0.0F, 0.0F, 4, 12, 4);

		this.armR = new ModelRenderer(this, 48, 16);
		this.armR.setRotationPoint(7.0F, -22.0F, 0.0F);
		this.armR.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
		this.armR.mirror = true;

		this.lowerArmR = new ModelRenderer(this, 48, 28);
		this.lowerArmR.setRotationPoint(-7.0F, -12.0F, 0.0F);
		this.lowerArmR.addBox(6.0F, 22.0F, -2.0F, 4, 12, 4);

		this.legL = new ModelRenderer(this, 0, 16);
		this.legL.setRotationPoint(-1.9F, 0.0F, 0.1F);
		this.legL.addBox(-2.0F, 0.0F, -2.0F, 4, 24, 4);
		this.legR = new ModelRenderer(this, 0, 16);
		this.legR.setRotationPoint(1.9F, 0.0F, 0.1F);
		this.legR.addBox(-2.0F, 0.0F, -2.0F, 4, 24, 4);
		this.legR.mirror = true;



		this.hat = new ModelRenderer(this, 32, 0);
		this.hat.setRotationPoint(0.0F, -24.0F, 0.0F);
		this.hat.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);

		this.armR.addChild(this.lowerArmR);
		this.armL.addChild(this.lowerArmL);
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
		this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, rotationYaw, rotationPitch, scale, entity); //Does the animation

		GlStateManager.pushMatrix();

		boolean blends = false; //There's probably no need to do all that unless we're using it, right?
		float alpha = 1.0f; //Between 0-1, higher than 1 presumably renders in the fourth dimension

		if (blends) {
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
			this.armL.render(scale);
			GlStateManager.disableBlend();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
			this.legL.render(scale);
			GlStateManager.disableBlend();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
			this.head.render(scale);
			GlStateManager.disableBlend();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
			this.upperTorso.render(scale);
			GlStateManager.disableBlend();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
			this.armR.render(scale);
			GlStateManager.disableBlend();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
			this.legR.render(scale);
			GlStateManager.disableBlend();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
			this.hat.render(scale);
			GlStateManager.disableBlend();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
			this.lowertorso.render(scale);
			GlStateManager.disableBlend();
		}else{

			this.armL.render(scale);
			this.legL.render(scale);
			this.head.render(scale);
			this.upperTorso.render(scale);
			this.armR.render(scale);
			this.legR.render(scale);
			this.hat.render(scale);
			this.lowertorso.render(scale);
		}

		GlStateManager.popMatrix();
	}

	public void setVisible(boolean visible) { //Now you see me, now you don't. Probably.
		this.upperTorso.showModel = visible;
		this.head.showModel = visible;
		this.hat.showModel = visible;
		this.legL.showModel = visible;
		this.legR.showModel = visible;
		this.armL.showModel = visible;
		this.armR.showModel = visible;
		this.lowertorso.showModel = visible;
	}

	public void setRotation(ModelRenderer modelRenderer, float x, float y, float z) { //You spin me right round
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		float swing = limbSwingAmount * 0.7f; //Tone that down, please.
		this.head.rotateAngleY = netHeadYaw * 0.017453292F;
		this.head.rotateAngleX = headPitch * 0.017453292F;
		this.legL.rotateAngleX = -1.5F * this.triangleWave(limbSwing, 13.0F) * swing;
		this.legR.rotateAngleX = 1.5F * this.triangleWave(limbSwing, 13.0F) * swing;
		this.legL.rotateAngleY = 0.0F;
		this.legR.rotateAngleY = 0.0F;

		this.armR.rotateAngleZ = 0.0F;
		this.armL.rotateAngleZ = 0.0F;
		this.armR.rotateAngleZ += MathHelper.cos(ageInTicks * 0.08F) * 0.03F - 0.05F;
		this.armL.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.08F) * 0.03F - 0.05F;
		this.armR.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.03F;
		this.armL.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.03F;

//		this.lowerArmL.offsetY = -2F;
//		this.lowerArmL.setRotationPoint(-8.0F, 22.0F, 1.0F);
		this.lowerArmL.setRotationPoint(-3.0F, 10.0F, -2.0F); //Front left corner of elbow when looking at front of celestial
//		this.lowerArmL.rotateAngleX = -2.5F;

//		this.lowerArmR.rotateAngleZ += MathHelper.sin(ageInTicks * 0.05F) * 0.03F;
//		this.lowerArmL.rotateAngleZ += 0.01F;
		this.lowerArmL.rotateAngleX += 0.01F;

//		Etheric.logger.log(Level.INFO, "ageInTicks: " + ageInTicks);
//		Etheric.logger.log(Level.INFO, "Cos: " + MathHelper.cos(ageInTicks));
//		Etheric.logger.log(Level.INFO, "CosMuted: " + MathHelper.cos(ageInTicks * 0.08F));
//		Etheric.logger.log(Level.INFO, "CosPostProcess1: " + MathHelper.cos(ageInTicks * 0.08F) * 0.03F);
//		Etheric.logger.log(Level.INFO, "CosPostProcessed: " + (MathHelper.cos(ageInTicks * 0.08F) * 0.03F - 0.05f));
	}

	public void setLivingAnimations(EntityLivingBase entityIn, float limbSwing, float limbSwingAmount, float partialTickTime) {
		EntityCelestial celestial = (EntityCelestial) entityIn;
		float swing = limbSwingAmount * 0.7f;
		int i = celestial.getAttackTimer();
		if (i > 0) {
			this.armR.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float) i - partialTickTime, 10.0F);
			this.armL.rotateAngleX = -2.0F + 1.5F * this.triangleWave((float) i - partialTickTime, 10.0F);
		} else {
			this.armR.rotateAngleX = (-0.2F + 1.5F * this.triangleWave(limbSwing, 13.0F)) * swing;
			this.armL.rotateAngleX = (-0.2F - 1.5F * this.triangleWave(limbSwing, 13.0F)) * swing;
		}
	}

	private float triangleWave(float p1, float p2) {
		return (Math.abs(p1 % p2 - p2 * 0.5F) - p2 * 0.25F) / (p2 * 0.25F);
	}
}

