package com.balsdon.androidallyplugin.ui.panel

import android.databinding.tool.ext.toCamelCase
import com.balsdon.androidallyplugin.adb.AdbScript
import com.balsdon.androidallyplugin.adb.parameters.AdbKeyCode
import com.balsdon.androidallyplugin.adb.parameters.SliderMode
import com.balsdon.androidallyplugin.adb.parameters.TalkBackAction
import com.balsdon.androidallyplugin.adb.parameters.TalkBackGranularity
import com.balsdon.androidallyplugin.adb.parameters.TalkBackSetting
import com.balsdon.androidallyplugin.adb.parameters.TalkBackVolumeSetting
import com.balsdon.androidallyplugin.adb.talkBackService
import com.balsdon.androidallyplugin.controller.Controller
import com.balsdon.androidallyplugin.localize
import com.balsdon.androidallyplugin.ui.CustomIcon
import com.balsdon.androidallyplugin.ui.component.IconButton
import com.balsdon.androidallyplugin.utils.addKeyAndActionListener
import com.balsdon.androidallyplugin.utils.createDropDownMenu
import com.balsdon.androidallyplugin.utils.createToggleRow
import com.balsdon.androidallyplugin.utils.placeComponent
import com.balsdon.androidallyplugin.utils.setMaxComponentSize
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import java.awt.GridBagConstraints
import java.awt.GridBagLayout


/**
 * Creates the TalkBack panel
 *
 * Cannot make this an invokable object according to [best practice](https://plugins.jetbrains.com/docs/intellij/plugin-extensions.html#implementing-extension)
 */
class TalkBackPanel(controller: Controller) : ControllerPanel(controller) {
    private val talkBackLabelString = localize("panel.talkback.label.talkback")
    private val talkBackOnButtonText = localize("panel.talkback.button.talkback.on")
    private val talkBackOffButtonText = localize("panel.talkback.button.talkback.off")

    private val talkBackSpeechOutputLabelString = localize("panel.talkback.label.talkback.speech")
    private val talkBackSpeechOutputOnButtonText = localize("panel.talkback.button.talkback.speech.on")
    private val talkBackSpeechOutputOffButtonText = localize("panel.talkback.button.talkback.speech.off")

    private val talkBackVolumeLabelString = localize("panel.talkback.label.talkback.volume")
    private val talkBackVolumeMediumButtonText = localize("panel.talkback.button.talkback.volume.medium")
    private val talkBackVolumeLowButtonText = localize("panel.talkback.button.talkback.volume.low")

    private val sliderControlsLabelString = localize("panel.talkback.label.slider.controls")
    private val sliderMinButtonText = localize("panel.talkback.button.slider.min")
    private val sliderDownButtonText = localize("panel.talkback.button.slider.down")
    private val sliderCenterButtonText = localize("panel.talkback.button.slider.middle")
    private val sliderUpButtonText = localize("panel.talkback.button.slider.up")
    private val sliderMaxButtonText = localize("panel.talkback.button.slider.max")
    private val navigationLabelString = localize("panel.talkback.label.talkback.navigation")
    private val previousButtonText = localize("panel.talkback.button.previous")
    private val nextButtonText = localize("panel.talkback.button.next")
    private val swipeUpButtonText = localize("panel.talkback.button.swipe_up")
    private val swipeDownButtonText = localize("panel.talkback.button.swipe_down")
    private val tapButtonText = localize("panel.talkback.button.tap")
    private val longTapButtonText = localize("panel.talkback.button.longTap")
    private val homeButtonText = localize("panel.talkback.button.home")
    private val backButtonText = localize("panel.talkback.button.back")
    private val menuButtonText = localize("panel.talkback.button.menu")
    private val actionsButtonText = localize("panel.talkback.button.actions")
    private val talkBackBlockOutLabelString = localize("panel.talkback.blockout")
    private val talkBackBlockOutOnText = localize("panel.talkback.blockout.on")
    private val talkBackBlockOutOffText = localize("panel.talkback.blockout.off")

    private val granularityLabelString = localize("panel.talkback.label.granularity")
    private val granularityOptions = listOf(
        "panel.talkback.label.granularity.default",
        "panel.talkback.label.granularity.headings",
        "panel.talkback.label.granularity.controls",
        "panel.talkback.label.granularity.links",
        "panel.talkback.label.granularity.words",
        "panel.talkback.label.granularity.paragraphs",
        "panel.talkback.label.granularity.characters",
        "panel.talkback.label.granularity.lines",
        "panel.talkback.label.granularity.window"
    )
    private var selectedGranularity: TalkBackGranularity = TalkBackGranularity.Default

    override fun buildComponent() = JPanel().apply {
        layout = GridBagLayout()
        addTalkBackToggleComponent(0)
        addGranularityCombo(1) { option ->
            selectedGranularity = TalkBackGranularity.valueOf(
                option.replace("panel.talkback.label.granularity.", "").toCamelCase()
            )
        }
        addBasicControls(whichRow = 2)
        addAdvancedControls(whichRow = 3)
        addSliderControlsComponent(whichRow = 4)
        addVolumeToggleComponent(whichRow = 5)
        addSpeechOutputToggleComponent(whichRow = 6)
        addBlockOutComponent(whichRow = 7)
    }


    private fun JPanel.addTalkBackToggleComponent(whichRow: Int) {
        createToggleRow(
            talkBackLabelString,
            whichRow,
            talkBackOnButtonText,
            talkBackOffButtonText,
            colSpan = 2,
            secondColSpan = 3,
            positiveAction = { talkBackService(true).run() },
            negativeAction = { talkBackService(false).run() }
        )
    }

    private fun JPanel.addSpeechOutputToggleComponent(whichRow: Int) {
        createToggleRow(
            talkBackSpeechOutputLabelString,
            whichRow,
            talkBackSpeechOutputOnButtonText,
            talkBackSpeechOutputOffButtonText,
            colSpan = 2,
            secondColSpan = 3,
            positiveAction = {
                AdbScript.TalkBackChangeSetting(TalkBackSetting.TOGGLE_SPEECH_OUTPUT, true).run()
            },
            negativeAction = {
                AdbScript.TalkBackChangeSetting(TalkBackSetting.TOGGLE_SPEECH_OUTPUT, false).run()
            }
        )
    }

    @Suppress("MagicNumber")
    private fun JPanel.addSliderControlsComponent(whichRow: Int) {
        placeComponent(
            JLabel(sliderControlsLabelString).apply { setMaxComponentSize() },
            x = 0, y = whichRow, 2, anchorType = GridBagConstraints.CENTER
        )
        val startIndex = 3
        listOf(
            sliderMinButtonText to { AdbScript.TalkBackSlider(SliderMode.MIN).run() },
            sliderDownButtonText to { AdbScript.TalkBackSlider(SliderMode.DECREASE).run() },
            sliderCenterButtonText to { AdbScript.TalkBackSlider(SliderMode.MID).run() },
            sliderUpButtonText to { AdbScript.TalkBackSlider(SliderMode.INCREASE).run() },
            sliderMaxButtonText to { AdbScript.TalkBackSlider(SliderMode.MAX).run() }
        ).forEachIndexed { index, (label, action) ->
            placeComponent(
                JButton(label).apply {
                    addKeyAndActionListener(action)
                },
                x = startIndex + index, y = whichRow,  1
            )
        }
    }

    private fun JPanel.addVolumeToggleComponent(whichRow: Int) {
        createToggleRow(
            talkBackVolumeLabelString,
            whichRow,
            talkBackVolumeMediumButtonText,
            talkBackVolumeLowButtonText,
            colSpan = 2,
            secondColSpan = 3,
            positiveAction = {
                AdbScript.TalkBackSetVolume(TalkBackVolumeSetting.VOLUME_HALF).run()
            },
            negativeAction = {
                AdbScript.TalkBackSetVolume(TalkBackVolumeSetting.VOLUME_MIN).run()
            }
        )
    }

    private fun JPanel.addGranularityCombo(whichRow: Int, onOption: (String) -> Unit) {
        createDropDownMenu(granularityLabelString, whichRow, granularityOptions, width = 5, onOption)
    }

    private fun JPanel.addBlockOutComponent(whichRow: Int) {
        createToggleRow(
            talkBackBlockOutLabelString,
            whichRow,
            talkBackBlockOutOnText,
            talkBackBlockOutOffText,
            colSpan = 2,
            secondColSpan = 3,
            positiveAction = {
                AdbScript.TalkBackChangeSetting(TalkBackSetting.BLOCK_OUT, true).run()
            },
            negativeAction = {
                AdbScript.TalkBackChangeSetting(TalkBackSetting.BLOCK_OUT, false).run()
            }
        )
    }

    private fun JPanel.addBasicControls(whichRow: Int) {
        placeComponent(
            JLabel(navigationLabelString).apply { setMaxComponentSize() },
            x = 0, y = whichRow, 2
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_SWIPE_LEFT,
                previousButtonText,
                ""
            ) {
                AdbScript.TalkBackUserAction(TalkBackAction.PREVIOUS, selectedGranularity).run()
            }.create(),
            x = 3, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_SWIPE_RIGHT,
                nextButtonText,
                ""
            ) {
                AdbScript.TalkBackUserAction(TalkBackAction.NEXT, selectedGranularity).run()
            }.create(),
            x = 4, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_TAP,
                tapButtonText,
                ""
            ) {
                AdbScript.TalkBackUserAction(TalkBackAction.PERFORM_CLICK_ACTION).run()
            }.create(),
            x = 5, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_TAP_LONG,
                longTapButtonText,
                ""
            ) { AdbScript.TalkBackUserAction(TalkBackAction.PERFORM_LONG_CLICK_ACTION).run() }.create(),
            x = 6, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_SWIPE_UP,
                swipeUpButtonText,
                ""
            ) {
                AdbScript.TalkBackUserAction(TalkBackAction.SCROLL_UP).run()
            }.create(),
            x = 7, y = whichRow, fillType = GridBagConstraints.BOTH
        )
    }

    private fun JPanel.addAdvancedControls(whichRow: Int) {
        placeComponent(
            IconButton(
                CustomIcon.DEVICE_BACK,
                backButtonText,
                ""
            ) { AdbScript.PressKeyAdb(AdbKeyCode.BACK).run() }.create(),
            x = 3, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.DEVICE_HOME,
                homeButtonText,
                ""
            ) { AdbScript.PressKeyAdb(AdbKeyCode.HOME).run() }.create(),
            x = 4, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_OPEN_MENU,
                menuButtonText,
                ""
            ) { AdbScript.TalkBackUserAction(TalkBackAction.TALKBACK_BREAKOUT).run() }.create(),
            x = 5, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_ACTIONS,
                actionsButtonText,
                ""
            ) { AdbScript.TalkBackUserAction(TalkBackAction.SHOW_CUSTOM_ACTIONS).run() }.create(),
            x = 6, y = whichRow, fillType = GridBagConstraints.BOTH
        )
        placeComponent(
            IconButton(
                CustomIcon.A11Y_SWIPE_DOWN,
                swipeDownButtonText,
                ""
            ) {
                AdbScript.TalkBackUserAction(TalkBackAction.SCROLL_DOWN).run()
            }.create(),
            x = 7, y = whichRow, fillType = GridBagConstraints.BOTH
        )
    }
}