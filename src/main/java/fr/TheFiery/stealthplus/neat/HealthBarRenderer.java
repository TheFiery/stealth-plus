package fr.TheFiery.stealthplus.neat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import java.awt.Color;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

public class HealthBarRenderer {
  @SubscribeEvent
  public void onRenderWorldLast(RenderWorldLastEvent event) {
    Minecraft mc = Minecraft.getMinecraft();
    if (!NeatConfig.renderInF1 && !Minecraft.isGuiEnabled())
      return; 
    EntityLivingBase cameraEntity = mc.renderViewEntity;
    Vec3 renderingVector = cameraEntity.getPosition(event.partialTicks);
    Frustrum frustrum = new Frustrum();
    double viewX = cameraEntity.lastTickPosX + (cameraEntity.posX - cameraEntity.lastTickPosX) * event.partialTicks;
    double viewY = cameraEntity.lastTickPosY + (cameraEntity.posY - cameraEntity.lastTickPosY) * event.partialTicks;
    double viewZ = cameraEntity.lastTickPosZ + (cameraEntity.posZ - cameraEntity.lastTickPosZ) * event.partialTicks;
    frustrum.setPosition(viewX, viewY, viewZ);
    WorldClient client = mc.theWorld;
    Set<Entity> entities = (Set<Entity>)ReflectionHelper.getPrivateValue(WorldClient.class, client, new String[] { "entityList", "field_73032_d", "J" });
    for (Entity entity : entities) {
      if (entity != null && entity instanceof EntityLiving && entity.isInRangeToRender3d(renderingVector.xCoord, renderingVector.yCoord, renderingVector.zCoord) && (entity.ignoreFrustumCheck || frustrum.isBoundingBoxInFrustum(entity.boundingBox)) && entity.isEntityAlive())
        renderHealthBar((EntityLivingBase)entity, event.partialTicks, (Entity)cameraEntity); 
    } 
  }
  
  public void renderHealthBar(EntityLivingBase passedEntity, float partialTicks, Entity viewPoint) {
    if (passedEntity.riddenByEntity != null)
      return; 
    EntityLivingBase entity = passedEntity;
    while (entity.ridingEntity != null && entity.ridingEntity instanceof EntityLivingBase)
      entity = (EntityLivingBase)entity.ridingEntity; 
    Minecraft mc = Minecraft.getMinecraft();
    float pastTranslate = 0.0F;
    while (entity != null) {
      float distance = passedEntity.getDistanceToEntity(viewPoint);
      if (distance <= NeatConfig.maxDistance && passedEntity.canEntityBeSeen(viewPoint) && !entity.isInvisible())
        if (NeatConfig.showOnBosses || !(entity instanceof net.minecraft.entity.boss.IBossDisplayData))
          if (NeatConfig.showOnPlayers || !(entity instanceof net.minecraft.entity.player.EntityPlayer)) {
            double x = passedEntity.lastTickPosX + (passedEntity.posX - passedEntity.lastTickPosX) * partialTicks;
            double y = passedEntity.lastTickPosY + (passedEntity.posY - passedEntity.lastTickPosY) * partialTicks;
            double z = passedEntity.lastTickPosZ + (passedEntity.posZ - passedEntity.lastTickPosZ) * partialTicks;
            float scale = 0.026666673F;
            float maxHealth = entity.getMaxHealth();
            float health = Math.min(maxHealth, entity.getHealth());
            if (maxHealth > 0.0F) {
              float percent = (int)(health / maxHealth * 100.0F);
              GL11.glPushMatrix();
              GL11.glTranslatef((float)(x - RenderManager.renderPosX), (float)(y - RenderManager.renderPosY + passedEntity.height + NeatConfig.heightAbove), (float)(z - RenderManager.renderPosZ));
              GL11.glNormal3f(0.0F, 1.0F, 0.0F);
              GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
              GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
              GL11.glScalef(-scale, -scale, scale);
              GL11.glDisable(2896);
              GL11.glDepthMask(false);
              GL11.glDisable(2929);
              GL11.glDisable(3553);
              GL11.glEnable(3042);
              GL11.glBlendFunc(770, 771);
              Tessellator tessellator = Tessellator.instance;
              float padding = NeatConfig.backgroundPadding;
              int bgHeight = NeatConfig.backgroundHeight;
              int barHeight = NeatConfig.barHeight;
              float size = NeatConfig.plateSize;
              int r = 0;
              int g = 255;
              int b = 0;
              ItemStack stack = null;
              if (entity instanceof net.minecraft.entity.monster.IMob) {
                r = 255;
                g = 0;
                EnumCreatureAttribute attr = entity.getCreatureAttribute();
                switch (attr) {
                  case ARTHROPOD:
                    stack = new ItemStack(Items.spider_eye);
                    break;
                  case UNDEAD:
                    stack = new ItemStack(Items.rotten_flesh);
                    break;
                  default:
                    stack = new ItemStack(Items.skull, 1, 4);
                    break;
                } 
              } 
              if (entity instanceof net.minecraft.entity.boss.IBossDisplayData) {
                stack = new ItemStack(Items.skull);
                size = NeatConfig.plateSizeBoss;
                r = 128;
                g = 0;
                b = 128;
              } 
              int armor = entity.getTotalArmorValue();
              boolean useHue = !NeatConfig.colorByType;
              if (useHue) {
                float hue = Math.max(0.0F, health / maxHealth / 3.0F - 0.07F);
                Color color = Color.getHSBColor(hue, 1.0F, 1.0F);
                r = color.getRed();
                g = color.getGreen();
                b = color.getBlue();
              } 
              GL11.glTranslatef(0.0F, pastTranslate, 0.0F);
              float s = 0.5F;
              String name = StatCollector.translateToLocal("entity." + EntityList.getEntityString((Entity)entity) + ".name");
              if (entity instanceof EntityLiving && ((EntityLiving)entity).hasCustomNameTag())
                name = EnumChatFormatting.ITALIC + ((EntityLiving)entity).getCustomNameTag(); 
              float namel = mc.fontRenderer.getStringWidth(name) * s;
              if (namel + 20.0F > size * 2.0F)
                size = namel / 2.0F + 10.0F; 
              float healthSize = size * health / maxHealth;
              if (NeatConfig.drawBackground) {
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA(0, 0, 0, 64);
                tessellator.addVertex((-size - padding), -bgHeight, 0.0D);
                tessellator.addVertex((-size - padding), (barHeight + padding), 0.0D);
                tessellator.addVertex((size + padding), (barHeight + padding), 0.0D);
                tessellator.addVertex((size + padding), -bgHeight, 0.0D);
                tessellator.draw();
              } 
              tessellator.startDrawingQuads();
              tessellator.setColorRGBA(127, 127, 127, 127);
              tessellator.addVertex(-size, 0.0D, 0.0D);
              tessellator.addVertex(-size, barHeight, 0.0D);
              tessellator.addVertex(size, barHeight, 0.0D);
              tessellator.addVertex(size, 0.0D, 0.0D);
              tessellator.draw();
              tessellator.startDrawingQuads();
              tessellator.setColorRGBA(r, g, b, 127);
              tessellator.addVertex(-size, 0.0D, 0.0D);
              tessellator.addVertex(-size, barHeight, 0.0D);
              tessellator.addVertex((healthSize * 2.0F - size), barHeight, 0.0D);
              tessellator.addVertex((healthSize * 2.0F - size), 0.0D, 0.0D);
              tessellator.draw();
              GL11.glEnable(3553);
              GL11.glPushMatrix();
              GL11.glTranslatef(-size, -4.5F, 0.0F);
              GL11.glScalef(s, s, s);
              mc.fontRenderer.drawString(name, 0, 0, 16777215);
              GL11.glPushMatrix();
              float s1 = 0.75F;
              GL11.glScalef(s1, s1, s1);
              int h = NeatConfig.hpTextHeight;
              String maxHpStr = EnumChatFormatting.BOLD + "" + (Math.round(maxHealth * 100.0D) / 100.0D);
              String hpStr = "" + (Math.round(health * 100.0D) / 100.0D);
              String percStr = (int)percent + "%";
              if (maxHpStr.endsWith(".0"))
                maxHpStr = maxHpStr.substring(0, maxHpStr.length() - 2); 
              if (hpStr.endsWith(".0"))
                hpStr = hpStr.substring(0, hpStr.length() - 2); 
              if (NeatConfig.showCurrentHP)
                mc.fontRenderer.drawString(hpStr, 2, h, 16777215); 
              if (NeatConfig.showMaxHP)
                mc.fontRenderer.drawString(maxHpStr, (int)(size / s * s1 * 2.0F) - 2 - mc.fontRenderer.getStringWidth(maxHpStr), h, 16777215); 
              if (NeatConfig.showPercentage)
                mc.fontRenderer.drawString(percStr, (int)(size / s * s1) - mc.fontRenderer	.getStringWidth(percStr) / 2, h, -1); 
              GL11.glPopMatrix();
              GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
              int off = 0;
              s1 = 0.5F;
              GL11.glScalef(s1, s1, s1);
              GL11.glTranslatef(size / s * s1 * 2.0F - 16.0F, 0.0F, 0.0F);
              mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
              if (stack != null && NeatConfig.showAttributes) {
                RenderItem.getInstance().renderIcon(off, 0, stack.getIconIndex(), 16, 16);
                off -= 16;
              } 
              if (armor > 0 && NeatConfig.showArmor) {
                int ironArmor = armor % 5;
                int diamondArmor = armor / 5;
                if (!NeatConfig.groupArmor) {
                  ironArmor = armor;
                  diamondArmor = 0;
                } 
                stack = new ItemStack((Item)Items.iron_chestplate);
                int i;
                for (i = 0; i < ironArmor; i++) {
                  RenderItem.getInstance().renderIcon(off, 0, stack.getIconIndex(), 16, 16);
                  off -= 4;
                } 
                stack = new ItemStack((Item)Items.diamond_chestplate);
                for (i = 0; i < diamondArmor; i++) {
                  RenderItem.getInstance().renderIcon(off, 0, stack.getIconIndex(), 16, 16);
                  off -= 4;
                } 
              } 
              GL11.glPopMatrix();
              GL11.glEnable(2929);
              GL11.glDepthMask(true);
              GL11.glEnable(2896);
              GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
              GL11.glPopMatrix();
              pastTranslate = -((bgHeight + barHeight) + padding);
            } 
          }   
      Entity riddenBy = entity.riddenByEntity;
      if (riddenBy instanceof EntityLivingBase) {
        entity = (EntityLivingBase)riddenBy;
        continue;
      } 
      return;
    } 
  }
}