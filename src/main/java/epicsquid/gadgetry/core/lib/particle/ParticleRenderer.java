package epicsquid.gadgetry.core.lib.particle;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import epicsquid.gadgetry.core.GadgetryCore;
import epicsquid.gadgetry.core.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ParticleRenderer {
  ArrayList<Particle> particles = new ArrayList<Particle>();

  public void updateParticles() {
    boolean doRemove = false;
    for (int i = 0; i < particles.size(); i++) {
      doRemove = true;
      if (particles.get(i) != null) {
        if (particles.get(i) instanceof IParticle) {
          if (((IParticle) particles.get(i)).alive()) {
            particles.get(i).onUpdate();
            doRemove = false;
          }
        }
      }
      if (doRemove) {
        particles.remove(i);
      }
    }
  }

  public void renderParticles(EntityPlayer dumbplayer, float partialTicks) {
    float f = ActiveRenderInfo.getRotationX();
    float f1 = ActiveRenderInfo.getRotationZ();
    float f2 = ActiveRenderInfo.getRotationYZ();
    float f3 = ActiveRenderInfo.getRotationXY();
    float f4 = ActiveRenderInfo.getRotationXZ();
    EntityPlayer player = Minecraft.getMinecraft().player;
    if (player != null) {
      Particle.interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
      Particle.interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
      Particle.interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
      Particle.cameraViewDir = player.getLook(partialTicks);
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.alphaFunc(516, 0.003921569F);
      GlStateManager.disableCull();

      GlStateManager.depthMask(false);

      Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
      Tessellator tess = Tessellator.getInstance();
      BufferBuilder buffer = tess.getBuffer();

      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
      for (int i = 0; i < particles.size(); i++) {
        if (particles.get(i) instanceof IParticle) {
          if (!((IParticle) particles.get(i)).isAdditive()) {
            particles.get(i).renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3);
          }
        }
      }
      tess.draw();

      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
      for (int i = 0; i < particles.size(); i++) {
        if (particles.get(i) != null) {
          if (((IParticle) particles.get(i)).isAdditive()) {
            particles.get(i).renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3);
          }
        }
      }
      tess.draw();

      GlStateManager.disableDepth();
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
      for (int i = 0; i < particles.size(); i++) {
        if (particles.get(i) instanceof IParticle) {
          if (!((IParticle) particles.get(i)).isAdditive() && ((IParticle) particles.get(i)).renderThroughBlocks()) {
            particles.get(i).renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3);
          }
        }
      }
      tess.draw();

      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
      buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
      for (int i = 0; i < particles.size(); i++) {
        if (particles.get(i) != null) {
          if (((IParticle) particles.get(i)).isAdditive() && ((IParticle) particles.get(i)).renderThroughBlocks()) {
            particles.get(i).renderParticle(buffer, player, partialTicks, f, f4, f1, f2, f3);
          }
        }
      }
      tess.draw();
      GlStateManager.enableDepth();

      GlStateManager.enableCull();
      GlStateManager.depthMask(true);
      GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
      GlStateManager.disableBlend();
      GlStateManager.alphaFunc(516, 0.1F);
      GlStateManager.enableAlpha();
    }
  }

  public void spawnParticle(World world, String particle, double x, double y, double z, double vx, double vy, double vz, double... data) {
    if (GadgetryCore.proxy instanceof ClientProxy) {
      try {
        particles.add(ParticleRegistry.particles.get(particle).newInstance(world, x, y, z, vx, vy, vz, data));
      } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
}
