package mods.betterfoliage.loader;

import mods.betterfoliage.common.util.DeobfNames;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BetterFoliageTransformer extends EZTransformerBase {

	@MethodTransform(className="net.minecraft.client.renderer.RenderBlocks",
					 obf=@MethodMatch(name=DeobfNames.RB_RBBRT_NAME_OBF, signature=DeobfNames.RB_RBBRT_SIG_OBF),
					 deobf=@MethodMatch(name=DeobfNames.RB_RBBRT_NAME_MCP, signature=DeobfNames.RB_RBBRT_SIG_MCP),
					 log="Applying RenderBlocks.renderBlockByRenderType() render type ovverride")
	public void handleRenderBlockOverride(MethodNode method, boolean obf) {
		AbstractInsnNode invokeGetRenderType = findNext(method.instructions.getFirst(), matchInvokeAny());
		AbstractInsnNode storeRenderType = findNext(invokeGetRenderType, matchOpcode(Opcodes.ISTORE));
		insertAfter(method.instructions, storeRenderType,
			new VarInsnNode(Opcodes.ALOAD, 0),
			obf ? new FieldInsnNode(Opcodes.GETFIELD, DeobfNames.RB_NAME_OBF, DeobfNames.RB_BA_NAME_OBF, DeobfNames.RB_BA_SIG_OBF) :
				  new FieldInsnNode(Opcodes.GETFIELD, DeobfNames.RB_NAME_MCP, DeobfNames.RB_BA_NAME_MCP, DeobfNames.RB_BA_SIG_MCP),
			new VarInsnNode(Opcodes.ILOAD, 2),
			new VarInsnNode(Opcodes.ILOAD, 3),
			new VarInsnNode(Opcodes.ILOAD, 4),
			new VarInsnNode(Opcodes.ALOAD, 1),
			new VarInsnNode(Opcodes.ILOAD, 5),
			obf ? new MethodInsnNode(Opcodes.INVOKESTATIC, "mods/betterfoliage/client/BetterFoliageClient", "getRenderTypeOverride", DeobfNames.BFC_GRTO_SIG_OBF) :
				  new MethodInsnNode(Opcodes.INVOKESTATIC, "mods/betterfoliage/client/BetterFoliageClient", "getRenderTypeOverride", DeobfNames.BFC_GRTO_SIG_MCP),
			new VarInsnNode(Opcodes.ISTORE, 5)
		);
	}
	
	@MethodTransform(className="shadersmodcore.client.Shaders",
			 obf=@MethodMatch(name="pushEntity", signature=DeobfNames.SHADERS_PE_SIG_OBF),
			 deobf=@MethodMatch(name="pushEntity", signature=DeobfNames.SHADERS_PE_SIG_MCP),
			 log="Applying Shaders.pushEntity() block id ovverride")
	public void handleGLSLBlockIDOverride(MethodNode method, boolean obf) {
		AbstractInsnNode arrayStore = findNext(method.instructions.getFirst(), matchOpcode(Opcodes.IASTORE));
		insertAfter(method.instructions, arrayStore.getPrevious(),
			new VarInsnNode(Opcodes.ALOAD, 1),
			obf ? new MethodInsnNode(Opcodes.INVOKESTATIC, "mods/betterfoliage/client/BetterFoliageClient", "getGLSLBlockIdOverride", DeobfNames.BFC_GLSLID_SIG_OBF) :
				  new MethodInsnNode(Opcodes.INVOKESTATIC, "mods/betterfoliage/client/BetterFoliageClient", "getGLSLBlockIdOverride", DeobfNames.BFC_GLSLID_SIG_MCP)
		);
	}
}