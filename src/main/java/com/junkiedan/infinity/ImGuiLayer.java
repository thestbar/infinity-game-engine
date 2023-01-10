package com.junkiedan.infinity;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

public class ImGuiLayer {
     private long glfwWindow;
     private final String GLSL_VERSION = "#version 330 core";
     private final ImGuiImplGlfw imGuiImplGlfw = new ImGuiImplGlfw();
     private final ImGuiImplGl3 imGuiImplGl3 = new ImGuiImplGl3();

     public ImGuiLayer(long glfwWindow) {
         this.glfwWindow = glfwWindow;
     }

     public void initImGui() {
         ImGui.createContext();
         ImGuiIO io = ImGui.getIO();

         io.setIniFilename("imgui.ini");
         io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

         imGuiImplGlfw.init(this.glfwWindow, true);

         // Change fonts and resize them
         final ImFontAtlas fontAtlas = io.getFonts();
         final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be destroyed

         fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
         fontConfig.setPixelSnapH(true);

         fontAtlas.addFontFromFileTTF("assets/fonts/andale_mono.ttf", 16, fontConfig);



         imGuiImplGl3.init(GLSL_VERSION);
     }

     public void update(float dt, Scene currentScene) {
         imGuiImplGlfw.newFrame();
         ImGui.newFrame();

         currentScene.sceneImGui();
         ImGui.showDemoWindow();

         ImGui.render();
         imGuiImplGl3 .renderDrawData(ImGui.getDrawData());

         if(ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
             final long backupWindowPtr = org.lwjgl.glfw.GLFW.glfwGetCurrentContext();
             ImGui.updatePlatformWindows();
             ImGui.renderPlatformWindowsDefault();
             org.lwjgl.glfw.GLFW.glfwMakeContextCurrent(backupWindowPtr);
         }
     }
}
