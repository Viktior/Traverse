package prospector.traverse.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import prospector.shootingstar.ShootingStar;
import prospector.shootingstar.model.ModelCompound;
import prospector.traverse.core.TraverseConstants;
import prospector.traverse.core.TraverseTab;

public class BlockTraverse extends Block {

    public BlockTraverse(String name, Material material, SoundType soundType) {
        super(material);
        setRegistryName(new ResourceLocation(TraverseConstants.MOD_ID, name));
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(TraverseTab.TAB);
        setHardness(1.5F);
        setResistance(10.0F);
        setHarvestLevel("pickaxe", 0);
        setSoundType(soundType);
        ShootingStar.registerModel(new ModelCompound(TraverseConstants.MOD_ID, this));
    }
}