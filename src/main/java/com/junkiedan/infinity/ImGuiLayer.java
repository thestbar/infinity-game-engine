package com.junkiedan.infinity;

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

     private boolean showText = false;

     public ImGuiLayer(long glfwWindow) {
         this.glfwWindow = glfwWindow;
     }

     public void initImGui() {
         ImGui.createContext();
         ImGuiIO io = ImGui.getIO();
         io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);

         imGuiImplGlfw.init(glfwWindow, true);

         imGuiImplGl3.init(GLSL_VERSION);
     }

     public void update(float dt) {
         imGuiImplGlfw.newFrame();
         ImGui.newFrame();

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
