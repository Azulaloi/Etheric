package etheric.client.renderer.entity;

import etheric.Etheric;
import etheric.client.model.entity.ModelCelestial;
import etheric.client.model.entity.ModelLesserCelestial;
import etheric.common.entity.mob.EntityCelestial;
import etheric.common.entity.mob.EntityLesserCelestial;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderCelestial extends RenderLiving<EntityCelestial> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Etheric.MODID, "textures/mobs/celestial.png");

	public RenderCelestial(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelCelestial(), 0.5F);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityCelestial entity) {
		return TEXTURE;
	}
	
	public static class Factory implements IRenderFactory<EntityCelestial> {

		@Override
		public Render<EntityCelestial> createRenderFor(RenderManager manager) {
			return new RenderCelestial(manager);
		}
		
	}

}
