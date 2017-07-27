package etheric.common.entity.mob;

import com.google.common.base.Predicate;
import etheric.Etheric;
import etheric.RegistryManager;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntityCelestial extends EntityMob {

	private static final Predicate<Entity> NOT_CELESTIAL = new Predicate<Entity>() {
		public boolean apply(@Nullable Entity p_apply_1_) {
			return p_apply_1_ instanceof EntityLivingBase
					&& !(p_apply_1_ instanceof EntityCelestial)
					&& !(p_apply_1_ instanceof EntityLesserCelestial)
					&& ((EntityLivingBase) p_apply_1_).attackable();
		}
	};

	public EntityCelestial(World worldIn) {
		super(worldIn);
		this.setSize(0.8F, 3.75F);
		this.experienceValue = 100;
		this.stepHeight = 1.5F;
		this.jumpMovementFactor = 0.4F;
		EntityMoveHelper movehelper = this.getMoveHelper();
		EntityLookHelper lookHelper = this.getLookHelper();
	}

	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.2D, true));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.applyEntityAI();
	}

	protected void applyEntityAI() {
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, true, true, NOT_CELESTIAL));
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(55.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.345D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(5.0D);
//		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1.0D);
//		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(8.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.5D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
	}

	protected void entityInit() {
		super.entityInit();
	}

	@Override //Look at me. I am the BodyHelper now.
	protected EntityBodyHelper createBodyHelper() {
		return new CelestialBodyHelper(this);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount){
//		this.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + ((double)this.rand.nextFloat() - 0.5D) * (double)this.width, -this.motionX * 4.0D, 1.5D, -this.motionZ * 4.0D);
		return super.attackEntityFrom(source, amount);
	}

	protected SoundEvent getAmbientSound()
	{
		return SoundEvents.ENTITY_ELDER_GUARDIAN_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource source)
	{
		return SoundEvents.BLOCK_STONE_BREAK;
	}

	protected SoundEvent getDeathSound()
	{
		return SoundEvents.ENTITY_WITHER_DEATH;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return new ResourceLocation (Etheric.MODID + ":mobs/celestial");
	}
}
