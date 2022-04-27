package org.moon.figura.avatars.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import org.moon.figura.avatars.model.rendering.FiguraImmediateBuffer;
import org.moon.figura.avatars.model.rendering.ImmediateAvatarRenderer;
import org.moon.figura.avatars.model.rendering.texture.FiguraTextureSet;
import org.moon.figura.lua.LuaUtils;
import org.moon.figura.lua.LuaWhitelist;
import org.moon.figura.math.matrix.FiguraMat3;
import org.moon.figura.math.matrix.FiguraMat4;
import org.moon.figura.math.vector.FiguraVec2;
import org.moon.figura.math.vector.FiguraVec3;
import org.moon.figura.math.vector.FiguraVec4;
import org.terasology.jnlua.LuaRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@LuaWhitelist
public class FiguraModelPart {

    @LuaWhitelist
    public final String name;
    public final PartCustomization customization;
    public final int index;

    private final Map<String, FiguraModelPart> childCache = new HashMap<>();
    public final List<FiguraModelPart> children;

    private List<Integer> facesByTexture;

    public void pushVerticesImmediate(ImmediateAvatarRenderer avatarRenderer) {
        for (int i = 0; i < facesByTexture.size(); i++)
            avatarRenderer.pushFaces(i, facesByTexture.get(i));
    }

    //-- LUA BUSINESS --//

    @LuaWhitelist
    public static FiguraVec3 getPos(FiguraModelPart modelPart) {
        LuaUtils.nullCheck("getPos", "modelPart", modelPart);
        return modelPart.customization.getPos();
    }
    @LuaWhitelist
    public static void setPos(FiguraModelPart modelPart, FiguraVec3 pos) {
        LuaUtils.nullCheck("setPos", "modelPart", modelPart);
        LuaUtils.nullCheck("setPos", "pos", pos);
        modelPart.customization.setPos(pos);
    }
    @LuaWhitelist
    public static FiguraVec3 getRot(FiguraModelPart modelPart) {
        LuaUtils.nullCheck("getRot", "modelPart", modelPart);
        return modelPart.customization.getRot();
    }
    @LuaWhitelist
    public static void setRot(FiguraModelPart modelPart, FiguraVec3 rot) {
        LuaUtils.nullCheck("setRot", "modelPart", modelPart);
        LuaUtils.nullCheck("setRot", "rot", rot);
        modelPart.customization.setRot(rot);
    }
    @LuaWhitelist
    public static FiguraVec3 getScale(FiguraModelPart modelPart) {
        LuaUtils.nullCheck("getScale", "modelPart", modelPart);
        return modelPart.customization.getScale();
    }
    @LuaWhitelist
    public static void setScale(FiguraModelPart modelPart, FiguraVec3 scale) {
        LuaUtils.nullCheck("setScale", "modelPart", modelPart);
        LuaUtils.nullCheck("setScale", "scale", scale);
        modelPart.customization.setScale(scale);
    }
    @LuaWhitelist
    public static FiguraVec3 getPivot(FiguraModelPart modelPart) {
        LuaUtils.nullCheck("getPivot", "modelPart", modelPart);
        return modelPart.customization.getPivot();
    }
    @LuaWhitelist
    public static void setPivot(FiguraModelPart modelPart, FiguraVec3 pivot) {
        LuaUtils.nullCheck("setPivot", "modelPart", modelPart);
        LuaUtils.nullCheck("setPivot", "pivot", pivot);
        modelPart.customization.setPivot(pivot);
    }
    @LuaWhitelist
    public static FiguraMat4 getPositionMatrix(FiguraModelPart modelPart) {
        LuaUtils.nullCheck("getMatrix", "modelPart", modelPart);
        modelPart.customization.recalculate();
        return modelPart.customization.getPositionMatrix();
    }
    @LuaWhitelist
    public static FiguraMat4 getPositionMatrixRaw(FiguraModelPart modelPart) {
        LuaUtils.nullCheck("getMatrixRaw", "modelPart", modelPart);
        return modelPart.customization.getPositionMatrix();
    }
    @LuaWhitelist
    public static FiguraMat3 getNormalMatrix(FiguraModelPart modelPart) {
        LuaUtils.nullCheck("getMatrix", "modelPart", modelPart);
        modelPart.customization.recalculate();
        return modelPart.customization.getNormalMatrix();
    }
    @LuaWhitelist
    public static FiguraMat3 getNormalMatrixRaw(FiguraModelPart modelPart) {
        LuaUtils.nullCheck("getMatrixRaw", "modelPart", modelPart);
        return modelPart.customization.getNormalMatrix();
    }
    @LuaWhitelist
    public static void setMatrix(FiguraModelPart modelPart, FiguraMat4 matrix) {
        LuaUtils.nullCheck("setMatrix", "modelPart", modelPart);
        LuaUtils.nullCheck("setMatrix", "matrix", matrix);
        modelPart.customization.setMatrix(matrix);
    }
    //GetColor
    //SetColor
    //GetUV
    //SetUV
    //SetUVPixels (will set uv in pixels, automatically dividing by texture size)
    @LuaWhitelist
    public static boolean getEnabled(FiguraModelPart modelPart) {
        LuaUtils.nullCheck("getEnabled", "modelPart", modelPart);
        return modelPart.customization.visible;
    }
    @LuaWhitelist
    public static void setEnabled(FiguraModelPart modelPart, Boolean bool) {
        LuaUtils.nullCheck("setEnabled", "modelPart", modelPart);
        if (bool == null) bool = false;
        modelPart.customization.visible = bool;
    }
    @LuaWhitelist
    public static String getPrimaryRenderType(FiguraModelPart modelPart) {
        LuaUtils.nullCheck("getPrimaryRenderType", "modelPart", modelPart);
        return modelPart.customization.getPrimaryRenderType();
    }
    @LuaWhitelist
    public static String getSecondaryRenderType(FiguraModelPart modelPart) {
        LuaUtils.nullCheck("getSecondaryRenderType", "modelPart", modelPart);
        return modelPart.customization.getSecondaryRenderType();
    }
    @LuaWhitelist
    public static void setPrimaryRenderType(FiguraModelPart modelPart, String type) {
        LuaUtils.nullCheck("setPrimaryRenderType", "modelPart", modelPart);
        LuaUtils.nullCheck("setPrimaryRenderType", "type", type);
        if (!FiguraTextureSet.LEGAL_RENDER_TYPES.contains(type))
            throw new LuaRuntimeException("Illegal RenderType: \"" + type + "\".");
        modelPart.customization.setPrimaryRenderType(type);
    }
    @LuaWhitelist
    public static void setSecondaryRenderType(FiguraModelPart modelPart, String type) {
        LuaUtils.nullCheck("setSecondaryRenderType", "modelPart", modelPart);
        LuaUtils.nullCheck("setSecondaryRenderType", "type", type);
        if (!FiguraTextureSet.LEGAL_RENDER_TYPES.contains(type))
            throw new LuaRuntimeException("Illegal RenderType: \"" + type + "\".");
        modelPart.customization.setSecondaryRenderType(type);
    }

    //-- METAMETHODS --//
    @LuaWhitelist
    public static Object __index(FiguraModelPart modelPart, String key) {
        if (modelPart.childCache.containsKey(key))
            return modelPart.childCache.get(key);
        for (FiguraModelPart child : modelPart.children) {
            if (child.name.equals(key)) {
                modelPart.childCache.put(key, child);
                return child;
            }
        }
        modelPart.childCache.put(key, null);
        return null;
    }

    //-- READING METHODS FROM NBT --//

    private FiguraModelPart(String name, PartCustomization customization, int index, List<FiguraModelPart> children) {
        this.name = name;
        this.customization = customization;
        this.index = index;
        this.children = children;
    }

    public static FiguraModelPart read(CompoundTag partCompound, List<FiguraImmediateBuffer.Builder> bufferBuilders) {
        return read(partCompound, bufferBuilders, new int[] {0});
    }

    private static FiguraModelPart read(CompoundTag partCompound, List<FiguraImmediateBuffer.Builder> bufferBuilders, int[] index) {
        //Read name
        String name = partCompound.getString("name");

        //Read transformation
        PartCustomization customization = PartCustomization.of();
        FiguraVec3 target = FiguraVec3.of();
        readVec3(target, partCompound, "rot");
        customization.setRot(target);
        readVec3(target, partCompound, "piv");
        customization.setPivot(target);
        if (partCompound.contains("primary"))
            customization.setPrimaryRenderType(partCompound.getString("primary"));
        if (partCompound.contains("secondary"))
            customization.setPrimaryRenderType(partCompound.getString("secondary"));
        target.free();
        customization.needsMatrixRecalculation = true;

        //Read vertex data
        int newIndex = -1;
        List<Integer> facesByTexture = new ArrayList<>(0);
        if (hasCubeData(partCompound)) {
            readCuboid(facesByTexture, bufferBuilders, partCompound);
            newIndex = index[0]++;
        } else if (hasMeshData(partCompound)) {
            //TODO: read mesh
            newIndex = index[0]++;
        }

        //Read children
        ArrayList<FiguraModelPart> children = new ArrayList<>(0);
        if (partCompound.contains("chld")) {
            ListTag listTag = partCompound.getList("chld", Tag.TAG_COMPOUND);
            for (Tag tag : listTag)
                children.add(read((CompoundTag) tag, bufferBuilders, index));
        }

        FiguraModelPart result = new FiguraModelPart(name, customization, newIndex, children);
        result.facesByTexture = facesByTexture;
        return result;
    }

    private static void readVec3(FiguraVec3 target, CompoundTag tag, String name) {
        readVec3(target, tag, name, 0, 0, 0);
    }

    private static void readVec3(FiguraVec3 target, CompoundTag tag, String name, double defX, double defY, double defZ) {
        if (tag.contains(name)) {
            ListTag list = (ListTag) tag.get(name);
            switch (list.getElementType()) {
                case Tag.TAG_FLOAT -> target.set(list.getFloat(0), list.getFloat(1), list.getFloat(2));
                case Tag.TAG_INT -> target.set(list.getInt(0), list.getInt(1), list.getInt(2));
                case Tag.TAG_SHORT -> target.set(list.getShort(0), list.getShort(1), list.getShort(2));
                case Tag.TAG_BYTE -> target.set(
                        ((ByteTag) list.get(0)).getAsByte(),
                        ((ByteTag) list.get(1)).getAsByte(),
                        ((ByteTag) list.get(2)).getAsByte()
                );
            }
        } else {
            target.set(defX, defY, defZ);
        }
    }

    private static void readVec4(FiguraVec4 target, CompoundTag tag, String name) {
        if (tag.contains(name)) {
            ListTag list = (ListTag) tag.get(name);
            switch (list.getElementType()) {
                case Tag.TAG_FLOAT -> target.set(list.getFloat(0), list.getFloat(1), list.getFloat(2), list.getFloat(3));
                case Tag.TAG_INT -> target.set(list.getInt(0), list.getInt(1), list.getInt(2), list.getInt(3));
                case Tag.TAG_SHORT -> target.set(list.getShort(0), list.getShort(1), list.getShort(2), list.getShort(3));
                case Tag.TAG_BYTE -> target.set(
                        ((ByteTag) list.get(0)).getAsByte(),
                        ((ByteTag) list.get(1)).getAsByte(),
                        ((ByteTag) list.get(2)).getAsByte(),
                        ((ByteTag) list.get(3)).getAsByte()
                );
            }
        } else {
            target.set(0, 0, 0, 0);
        }
    }

    private static boolean hasCubeData(CompoundTag partCompound) {
        if (partCompound.contains("cube_data", Tag.TAG_COMPOUND))
            return !partCompound.getCompound("cube_data").isEmpty();
        return false;
    }

    private static boolean hasMeshData(CompoundTag partCompound) {
        if (partCompound.contains("mesh_data", Tag.TAG_COMPOUND))
            return !partCompound.getCompound("mesh_data").isEmpty();
        return false;
    }

    private static final Map<String, FiguraVec3[]> faceData = new ImmutableMap.Builder<String, FiguraVec3[]>()
            .put("n", new FiguraVec3[] {
                    FiguraVec3.of(1, 0, 0),
                    FiguraVec3.of(0, 0, 0),
                    FiguraVec3.of(0, 1, 0),
                    FiguraVec3.of(1, 1, 0),
                    FiguraVec3.of(0, 0, -1)
            })
            .put("s", new FiguraVec3[] {
                    FiguraVec3.of(0, 0, 1),
                    FiguraVec3.of(1, 0, 1),
                    FiguraVec3.of(1, 1, 1),
                    FiguraVec3.of(0, 1, 1),
                    FiguraVec3.of(0, 0, 1)
            })
            .put("e", new FiguraVec3[] {
                    FiguraVec3.of(1, 0, 1),
                    FiguraVec3.of(1, 0, 0),
                    FiguraVec3.of(1, 1, 0),
                    FiguraVec3.of(1, 1, 1),
                    FiguraVec3.of(1, 0, 0)
            })
            .put("w", new FiguraVec3[] {
                    FiguraVec3.of(0, 0, 0),
                    FiguraVec3.of(0, 0, 1),
                    FiguraVec3.of(0, 1, 1),
                    FiguraVec3.of(0, 1, 0),
                    FiguraVec3.of(-1, 0, 0)
            })
            .put("u", new FiguraVec3[] {
                    FiguraVec3.of(0, 1, 1),
                    FiguraVec3.of(1, 1, 1),
                    FiguraVec3.of(1, 1, 0),
                    FiguraVec3.of(0, 1, 0),
                    FiguraVec3.of(0, 1, 0)
            })
            .put("d", new FiguraVec3[] {
                    FiguraVec3.of(0, 0, 0),
                    FiguraVec3.of(1, 0, 0),
                    FiguraVec3.of(1, 0, 1),
                    FiguraVec3.of(0, 0, 1),
                    FiguraVec3.of(0, -1, 0)
            }).build();

    private static final FiguraVec2[] uvValues = new FiguraVec2[] {
            FiguraVec2.of(0, 1),
            FiguraVec2.of(1, 1),
            FiguraVec2.of(1, 0),
            FiguraVec2.of(0, 0)
    };

    private static final FiguraVec3 from = FiguraVec3.of();
    private static final FiguraVec3 to = FiguraVec3.of();
    private static final FiguraVec3 ftDiff = FiguraVec3.of();

    private static void readCuboid(List<Integer> facesByTexture, List<FiguraImmediateBuffer.Builder> builders, CompoundTag data) {
        //Read from and to
        readVec3(from, data, "f");
        readVec3(to, data, "t");

        //Read inflate
        double inflate = 0;
        if (data.contains("inf"))
            inflate = data.getFloat("inf");
        from.add(-inflate, -inflate, -inflate);
        to.add(inflate, inflate, inflate);

        //Cache difference between from and to
        ftDiff.set(to);
        ftDiff.subtract(from);

        //Iterate over faces, add them
        for (String direction : faceData.keySet())
            readFace(data.getCompound("cube_data"), facesByTexture, builders, direction);
    }

    private static final FiguraVec3 tempPos = FiguraVec3.of();
    private static final FiguraVec4 uv = FiguraVec4.of();

    private static void readFace(CompoundTag faces, List<Integer> facesByTexture, List<FiguraImmediateBuffer.Builder> builders, String direction) {
        if (faces.contains(direction)) {
            CompoundTag face = faces.getCompound(direction);
            short texId = face.getShort("tex");
            while (texId >= facesByTexture.size())
                facesByTexture.add(0);
            while (texId >= builders.size())
                builders.add(FiguraImmediateBuffer.builder());
            facesByTexture.set(texId, facesByTexture.get(texId) + 1);

            FiguraVec3 normal = faceData.get(direction)[4];
            int rotation = (int) (face.getFloat("rot") / 90f);
            readVec4(uv, face, "uv");
            for (int i = 0; i < 4; i++) {
                tempPos.set(ftDiff);
                tempPos.multiply(faceData.get(direction)[i]);
                tempPos.add(from);

                FiguraVec2 normalizedUv = uvValues[(i + rotation)%4];

                builders.get(texId).vertex(
                        (float) tempPos.x, (float) tempPos.y, (float) tempPos.z,
                        (float) Mth.lerp(normalizedUv.x, uv.x, uv.z),
                        (float) Mth.lerp(normalizedUv.y, uv.y, uv.w),
                        (float) normal.x, (float) normal.y, (float) normal.z
                );
            }
        }
    }

}
