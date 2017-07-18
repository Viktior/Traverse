package prospector.traverse.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import prospector.traverse.core.TraverseConstants;
import prospector.traverse.core.TraverseTab;
import prospector.traverse.util.PropertyString;

import java.util.Random;

public class BlockMetadata extends Block {

    public final PropertyString variant;
    public ResourceLocation drop = null;

    public BlockMetadata(String name, Material material, SoundType soundType, ResourceLocation drop, String... variants) {
        super(material);
        this.drop = drop;
        if (variants.length > 0) {
            variant = new PropertyString("variant", variants);
        } else {
            variant = null;
        }
        setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name));
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(TraverseTab.TAB);
        setHardness(1.5F);
        setResistance(10.0F);
        setHarvestLevel("pickaxe", 0);
        setSoundType(soundType);
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this));

    }

    public BlockMetadata(String name, Material material, SoundType soundType, String... variants) {
        this(name, material, soundType, null, variants);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (int meta = 0; meta < variant.getAsList().size(); meta++) {
            items.add(new ItemStack(this, 1, meta));
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (variant != null) {
            return variant.getAsList().indexOf(state.getValue(variant));
        } else {
            return super.getMetaFromState(state);
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (variant != null) {
            return getDefaultState().withProperty(variant, variant.getAsList().get(meta));
        } else {
            return super.getStateFromMeta(meta);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        if (variant != null) {
            return new BlockStateContainer(this, variant);
        } else {
            return super.createBlockState();
        }
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return drop != null ? ForgeRegistries.ITEMS.getValue(drop) : super.getItemDropped(state, rand, fortune);
    }
}
