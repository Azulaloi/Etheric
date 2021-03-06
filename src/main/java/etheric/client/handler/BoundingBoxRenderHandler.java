package etheric.client.handler;

import etheric.Etheric;
import etheric.RegistryManager;
import etheric.common.block.BlockPipe;
import etheric.common.tileentity.TileEntityPipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = Etheric.MODID, value = { Side.CLIENT })
public class BoundingBoxRenderHandler {

	@SubscribeEvent
	public static void onPipeBoundingBoxRender(DrawBlockHighlightEvent event) {
		if (event.getTarget() != null && event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos pos = event.getTarget().getBlockPos();
			EntityPlayer player = event.getPlayer();
			EnumFacing side = event.getTarget().sideHit;
			if (player.world.getBlockState(pos).getBlock() == RegistryManager.pipe
					&& player.world.getTileEntity(pos) != null) {
				TileEntityPipe te = (TileEntityPipe) player.world.getTileEntity(pos);
				float hitX = 0, hitY = 0, hitZ = 0;
				if (side.getAxis() != EnumFacing.Axis.X) {
					hitX = (float) event.getTarget().hitVec.x - pos.getX();
				}
				if (side.getAxis() != EnumFacing.Axis.Y) {
					hitY = (float) event.getTarget().hitVec.y - pos.getY();
				}
				if (side.getAxis() != EnumFacing.Axis.Z) {
					hitZ = (float) event.getTarget().hitVec.z - pos.getZ();
				}
				EnumFacing connection = te.viewedConnection(player, side, hitX, hitY, hitZ);
				event.setCanceled(true);

				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
						GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				GlStateManager.glLineWidth(2.0F);
				GlStateManager.disableTexture2D();
				GlStateManager.depthMask(false);

				double d0 = player.lastTickPosX
						+ (player.posX - player.lastTickPosX) * (double) event.getPartialTicks();
				double d1 = player.lastTickPosY
						+ (player.posY - player.lastTickPosY) * (double) event.getPartialTicks();
				double d2 = player.lastTickPosZ
						+ (player.posZ - player.lastTickPosZ) * (double) event.getPartialTicks();
				Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder vertexbuffer = tessellator.getBuffer();
				vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
				vertexbuffer.setTranslation(-d0, -d1, -d2);

				if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() == RegistryManager.tuning_fork
						|| player.getHeldItem(EnumHand.OFF_HAND).getItem() == RegistryManager.tuning_fork) {
					drawPipeConnectionAABB(vertexbuffer, connection, pos);
				} else {
					IBlockState blockstate = player.world.getBlockState(pos);
					IExtendedBlockState state = (IExtendedBlockState) blockstate.getBlock().getExtendedState(blockstate,
							player.world, pos);
					drawFullPipeAABB(vertexbuffer, state, pos, player.world);
				}

				tessellator.draw();
				vertexbuffer.setTranslation(0D, 0D, 0D);

				GlStateManager.depthMask(true);
				GlStateManager.enableTexture2D();
				GlStateManager.disableBlend();
			}
		}
	}

	private static void drawPipeConnectionAABB(BufferBuilder buffer, EnumFacing side, BlockPos pos) {
		double minX = -0.0020000000949949026D + pos.getX(), minY = -0.0020000000949949026D + pos.getY(),
				minZ = -0.0020000000949949026D + pos.getZ();
		double maxX = 1.0020000000949949026D + pos.getX(), maxY = 1.0020000000949949026D + pos.getY(),
				maxZ = 1.0020000000949949026D + pos.getZ();

		if (side == null) {
			minX = -0.0020000000949949026D + 0.3125 + pos.getX();
			maxX = 1.0020000000949949026D - 0.3125 + pos.getX();
			minY = -0.0020000000949949026D + 0.3125 + pos.getY();
			maxY = 1.0020000000949949026D - 0.3125 + pos.getY();
			minZ = -0.0020000000949949026D + 0.3125 + pos.getZ();
			maxZ = 1.0020000000949949026D - 0.3125 + pos.getZ();
		} else {
			if (side.getAxis() != EnumFacing.Axis.X) {
				minX = -0.0020000000949949026D + 0.3125 + pos.getX();
				maxX = 1.0020000000949949026D - 0.3125 + pos.getX();
			} else {
				if (side == EnumFacing.WEST) {
					maxX = -0.0020000000949949026D + 0.3125 + pos.getX();
				} else if (side == EnumFacing.EAST) {
					minX = 1.0020000000949949026D - 0.3125 + pos.getX();
				}
			}
			if (side.getAxis() != EnumFacing.Axis.Y) {
				minY = -0.0020000000949949026D + 0.3125 + pos.getY();
				maxY = 1.0020000000949949026D - 0.3125 + pos.getY();
			} else {
				if (side == EnumFacing.DOWN) {
					maxY = -0.0020000000949949026D + 0.3125 + pos.getY();
				} else if (side == EnumFacing.UP) {
					minY = 1.0020000000949949026D - 0.3125 + pos.getY();
				}
			}
			if (side.getAxis() != EnumFacing.Axis.Z) {
				minZ = -0.0020000000949949026D + 0.3125 + pos.getZ();
				maxZ = 1.0020000000949949026D - 0.3125 + pos.getZ();
			} else {
				if (side == EnumFacing.NORTH) {
					maxZ = -0.0020000000949949026D + 0.3125 + pos.getZ();
				} else if (side == EnumFacing.SOUTH) {
					minZ = 1.0020000000949949026D - 0.3125 + pos.getZ();
				}
			}
		}

		buffer.pos(minX, minY, minZ).color(0F, 0F, 0F, 0.0F).endVertex();
		buffer.pos(minX, minY, maxZ).color(0F, 0F, 0F, 0.4F).endVertex();
		buffer.pos(maxX, minY, maxZ).color(0F, 0F, 0F, 0.4F).endVertex();
		buffer.pos(maxX, minY, minZ).color(0F, 0F, 0F, 0.4F).endVertex();
		buffer.pos(minX, minY, minZ).color(0F, 0F, 0F, 0.4F).endVertex();
		buffer.pos(minX, maxY, minZ).color(0F, 0F, 0F, 0.4F).endVertex();
		buffer.pos(minX, minY, maxZ).color(0F, 0F, 0F, 0.0F).endVertex();
		buffer.pos(minX, maxY, maxZ).color(0F, 0F, 0F, 0.4F).endVertex();
		buffer.pos(maxX, minY, maxZ).color(0F, 0F, 0F, 0.0F).endVertex();
		buffer.pos(maxX, maxY, maxZ).color(0F, 0F, 0F, 0.4F).endVertex();
		buffer.pos(maxX, minY, minZ).color(0F, 0F, 0F, 0.0F).endVertex();
		buffer.pos(maxX, maxY, minZ).color(0F, 0F, 0F, 0.4F).endVertex();
		buffer.pos(minX, maxY, minZ).color(0F, 0F, 0F, 0.0F).endVertex();
		buffer.pos(minX, maxY, maxZ).color(0F, 0F, 0F, 0.4F).endVertex();
		buffer.pos(maxX, maxY, maxZ).color(0F, 0F, 0F, 0.4F).endVertex();
		buffer.pos(maxX, maxY, minZ).color(0F, 0F, 0F, 0.4F).endVertex();
		buffer.pos(minX, maxY, minZ).color(0F, 0F, 0F, 0.4F).endVertex();
	}

	private static void drawPartialPipeConnectionAABB(BufferBuilder buffer, EnumFacing side, BlockPos pos) {
		double minX = -0.0020000000949949026D + pos.getX(), minY = -0.0020000000949949026D + pos.getY(),
				minZ = -0.0020000000949949026D + pos.getZ();
		double maxX = 1.0020000000949949026D + pos.getX(), maxY = 1.0020000000949949026D + pos.getY(),
				maxZ = 1.0020000000949949026D + pos.getZ();
		double d = 0.3125;

		if (side != null) {
			switch (side) {
			case DOWN:
				buffer.pos(minX + d, minY, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX + d, minY, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, minY, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, minY, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, minY, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, minY + d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, minY, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX + d, minY + d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, minY, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX - d, minY + d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, minY, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX - d, minY + d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				break;
			case UP:
				buffer.pos(minX + d, maxY, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX + d, maxY, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, maxY, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, maxY, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, maxY, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, maxY, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX + d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, maxY, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX - d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, maxY, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX - d, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				break;
			case NORTH:
				buffer.pos(minX + d, minY + d, minZ).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX + d, maxY - d, minZ).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, maxY - d, minZ).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, minY + d, minZ).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, minY + d, minZ).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, minY + d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, maxY - d, minZ).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX + d, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, maxY - d, minZ).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX - d, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, minY + d, minZ).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX - d, minY + d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				break;
			case SOUTH:
				buffer.pos(minX + d, minY + d, maxZ).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX + d, maxY - d, maxZ).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, maxY - d, maxZ).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, minY + d, maxZ).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, minY + d, maxZ).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, minY + d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, maxY - d, maxZ).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX + d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, maxY - d, maxZ).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX - d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, minY + d, maxZ).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX - d, minY + d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				break;
			case WEST:
				buffer.pos(minX, minY + d, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX, minY + d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX, minY + d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX + d, minY + d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX, maxY - d, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX + d, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX, maxY - d, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX + d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(minX, minY + d, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(minX + d, minY + d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				break;
			case EAST:
				buffer.pos(maxX, minY + d, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX, minY + d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX, minY + d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX - d, minY + d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX, maxY - d, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX - d, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX, maxY - d, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX - d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				buffer.pos(maxX, minY + d, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
				buffer.pos(maxX - d, minY + d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
				break;
			}
		}
	}

	private static void drawFullPipeAABB(BufferBuilder buffer, IExtendedBlockState state, BlockPos pos, World world) {
		double minX = -0.0020000000949949026D + pos.getX(), minY = -0.0020000000949949026D + pos.getY(),
				minZ = -0.0020000000949949026D + pos.getZ();
		double maxX = 1.0020000000949949026D + pos.getX(), maxY = 1.0020000000949949026D + pos.getY(),
				maxZ = 1.0020000000949949026D + pos.getZ();
		double d = 0.3125;

		boolean[] connections = new boolean[6];
		TileEntity te = world.getTileEntity(pos);
		TileEntityPipe tep = null;
		if (te != null && te instanceof TileEntityPipe) {
			tep = (TileEntityPipe) te;
		}
		if (state.getValue(BlockPipe.CONNECTIONS[0]) > 0 || (tep != null && tep.connectionDisabled(EnumFacing.DOWN))) {
			drawPartialPipeConnectionAABB(buffer, EnumFacing.DOWN, pos);
			connections[0] = true;
		}
		if (state.getValue(BlockPipe.CONNECTIONS[1]) > 0 || (tep != null && tep.connectionDisabled(EnumFacing.UP))) {
			drawPartialPipeConnectionAABB(buffer, EnumFacing.UP, pos);
			connections[1] = true;
		}
		if (state.getValue(BlockPipe.CONNECTIONS[2]) > 0 || (tep != null && tep.connectionDisabled(EnumFacing.NORTH))) {
			drawPartialPipeConnectionAABB(buffer, EnumFacing.NORTH, pos);
			connections[2] = true;
		}
		if (state.getValue(BlockPipe.CONNECTIONS[3]) > 0 || (tep != null && tep.connectionDisabled(EnumFacing.SOUTH))) {
			drawPartialPipeConnectionAABB(buffer, EnumFacing.SOUTH, pos);
			connections[3] = true;
		}
		if (state.getValue(BlockPipe.CONNECTIONS[4]) > 0 || (tep != null && tep.connectionDisabled(EnumFacing.WEST))) {
			drawPartialPipeConnectionAABB(buffer, EnumFacing.WEST, pos);
			connections[4] = true;
		}
		if (state.getValue(BlockPipe.CONNECTIONS[5]) > 0 || (tep != null && tep.connectionDisabled(EnumFacing.EAST))) {
			drawPartialPipeConnectionAABB(buffer, EnumFacing.EAST, pos);
			connections[5] = true;
		}

		if (connections[0] == connections[2]) {
			buffer.pos(minX + d, minY + d, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(maxX - d, minY + d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
		if (connections[0] == connections[3]) {
			buffer.pos(maxX - d, minY + d, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(minX + d, minY + d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
		if (connections[0] == connections[4]) {
			buffer.pos(minX + d, minY + d, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(minX + d, minY + d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
		if (connections[0] == connections[5]) {
			buffer.pos(maxX - d, minY + d, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(maxX - d, minY + d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
		if (connections[1] == connections[2]) {
			buffer.pos(minX + d, maxY - d, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(maxX - d, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
		if (connections[1] == connections[3]) {
			buffer.pos(maxX - d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(minX + d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
		if (connections[1] == connections[4]) {
			buffer.pos(minX + d, maxY - d, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(minX + d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
		if (connections[1] == connections[5]) {
			buffer.pos(maxX - d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(maxX - d, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
		if (connections[2] == connections[4]) {
			buffer.pos(minX + d, minY + d, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(minX + d, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
		if (connections[2] == connections[5]) {
			buffer.pos(maxX - d, minY + d, minZ + d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(maxX - d, maxY - d, minZ + d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
		if (connections[3] == connections[4]) {
			buffer.pos(minX + d, minY + d, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(minX + d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
		if (connections[3] == connections[5]) {
			buffer.pos(maxX - d, minY + d, maxZ - d).color(0F, 0F, 0F, 0F).endVertex();
			buffer.pos(maxX - d, maxY - d, maxZ - d).color(0F, 0F, 0F, 0.4F).endVertex();
		}
	}

}
