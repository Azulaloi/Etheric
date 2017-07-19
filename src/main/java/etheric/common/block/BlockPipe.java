package etheric.common.block;

import etheric.common.block.property.UnlistedPropertyBool;
import etheric.common.block.property.UnlistedPropertyFloat;
import etheric.common.block.property.UnlistedPropertyInt;
import etheric.common.capabilty.IQuintessenceCapability;
import etheric.common.capabilty.QuintessenceCapabilityProvider;
import etheric.common.tileentity.TileEntityPipe;
import etheric.common.tileentity.TileEntityTestTank;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPipe extends BlockBase implements ITileEntityProvider {

	public static final UnlistedPropertyInt[] CONNECTIONS = { new UnlistedPropertyInt("down"),
			new UnlistedPropertyInt("up"), new UnlistedPropertyInt("north"), new UnlistedPropertyInt("south"),
			new UnlistedPropertyInt("west"), new UnlistedPropertyInt("east") };
	public static final UnlistedPropertyInt QUINTESSENCE = new UnlistedPropertyInt("quintessence");
	public static final UnlistedPropertyFloat PURITY = new UnlistedPropertyFloat("purity");
	public static final UnlistedPropertyBool[] QUINT_CONS = { new UnlistedPropertyBool("q_down"), 
			new UnlistedPropertyBool("q_up"), new UnlistedPropertyBool("q_north"), new UnlistedPropertyBool("q_south"),
			new UnlistedPropertyBool("q_west"), new UnlistedPropertyBool("q_east") };

	public BlockPipe(String name) {
		super(name, Material.ROCK);

		setSoundType(SoundType.GLASS);
		setHardness(2F);

		setDefaultState(this.blockState.getBaseState());
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityPipe();
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityPipe) {
			((TileEntityPipe) worldIn.getTileEntity(pos)).breakBlock(state);
		}
		worldIn.removeTileEntity(pos);
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedState = (IExtendedBlockState) state;
		for (int i = 0; i < CONNECTIONS.length; i++) {
			int connected = getConnection(world, pos, EnumFacing.getFront(i));
			extendedState = extendedState.withProperty(CONNECTIONS[i], connected);
		}
		if (world.getTileEntity(pos) != null) {
			TileEntityPipe te = (TileEntityPipe) world.getTileEntity(pos);
			IQuintessenceCapability teQ = te.getCapability(QuintessenceCapabilityProvider.quintessenceCapability, null);
			
			extendedState = extendedState.withProperty(QUINTESSENCE, teQ.getAmount());
			extendedState = extendedState.withProperty(PURITY, teQ.getPurity());
		}
		for (int i = 0; i < QUINT_CONS.length; i++) {
			extendedState = extendedState.withProperty(QUINT_CONS[i], getQuintConnection(world, pos, EnumFacing.getFront(i)));
		}
		
		return extendedState;
	}
	
	private int getConnection(IBlockAccess world, BlockPos pos, EnumFacing dir) {
		TileEntity te = world.getTileEntity(pos.offset(dir));
		if (world.getBlockState(pos.offset(dir)).getBlock() == this) {
			return 1;
		}
		if (te != null && te.hasCapability(QuintessenceCapabilityProvider.quintessenceCapability, dir)) {
			return 2;
		}
		
		return 0;
	}
	
	private boolean getQuintConnection(IBlockAccess world, BlockPos pos, EnumFacing dir) {
		TileEntity te = world.getTileEntity(pos.offset(dir));
		if (te != null && world.getBlockState(pos.offset(dir)).getBlock() == this) {
			return te.getCapability(QuintessenceCapabilityProvider.quintessenceCapability, dir.getOpposite()).getAmount() > 0;
		}
		if (te != null && te.hasCapability(QuintessenceCapabilityProvider.quintessenceCapability, dir.getOpposite())) {
			return true;
		}
		return false;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		super.neighborChanged(state, world, pos, block, fromPos);
		TileEntityPipe te = (TileEntityPipe) world.getTileEntity(pos);
		te.updateConnections();
		te.markDirty();
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		if (world.getTileEntity(pos) instanceof TileEntityPipe) {
			((TileEntityPipe) world.getTileEntity(pos)).updateConnections();
			world.getTileEntity(pos).markDirty();
		}
	}

	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		super.eventReceived(state, worldIn, pos, id, param);
		TileEntity tileentity = worldIn.getTileEntity(pos);
		return tileentity == null ? false : tileentity.receiveClientEvent(id, param);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState();
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		IUnlistedProperty[] unlisted = new IUnlistedProperty[] {
				CONNECTIONS[0],
				CONNECTIONS[1],
				CONNECTIONS[2],
				CONNECTIONS[3],
				CONNECTIONS[4],
				CONNECTIONS[5],
				QUINTESSENCE,
				PURITY,
				QUINT_CONS[0],
				QUINT_CONS[1],
				QUINT_CONS[2],
				QUINT_CONS[3],
				QUINT_CONS[4],
				QUINT_CONS[5]
		};
		return new ExtendedBlockState(this, new IProperty[] {}, unlisted);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		return ((TileEntityPipe) world.getTileEntity(pos)).activate(world, pos, state, player, hand, side, hitX, hitY,
				hitZ);
	}

}
