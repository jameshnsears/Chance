package com.github.jameshnsears.chance.ui.dialog.bag.card.side

import com.github.jameshnsears.chance.data.domain.core.Side
import com.github.jameshnsears.chance.data.domain.core.bag.testdouble.BagDataTestDouble
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagAndroidViewModel
import com.github.jameshnsears.chance.ui.dialog.bag.DialogBagUnitTestHelper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import kotlin.test.assertNotNull


class CardSideAndroidViewModelUnitTest : DialogBagUnitTestHelper() {
    @Test
    fun sideCardSideDescriptionInit() = runTest {
        val diceInDialogBag = BagDataTestDouble().d2
        val sideInDialogBag = diceInDialogBag.sides[0]

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, sideInDialogBag
        )

        val bagCardSideAndroidViewModel =
            dialogBagAndroidViewModel.cardSideAndroidViewModel

        assertEquals(
            diceInDialogBag.sides[0].description,
            bagCardSideAndroidViewModel.stateFlowCardSide.value.sideDescription
        )
    }

    @Test
    fun sideCardModifyAndReset() = runTest {
        val diceInDialogBag = BagDataTestDouble().d2
        val sideInDialogBag = diceInDialogBag.sides[0]

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, sideInDialogBag
        )
        assertStateSide(dialogBagAndroidViewModel, sideInDialogBag)

        val bagCardSideAndroidViewModel =
            dialogBagAndroidViewModel.cardSideAndroidViewModel

        val sideChanged = sideInDialogBag.copy(
            numberColour = "00000000",
            description = "updatedDescription",
            descriptionColour = "FFFFFFFF"
        )
        bagCardSideAndroidViewModel.sideNumberColour(sideChanged.numberColour)
        bagCardSideAndroidViewModel.sideDescription(sideChanged.description)
        bagCardSideAndroidViewModel.sideDescriptionColour(sideChanged.descriptionColour)

        assertStateSide(dialogBagAndroidViewModel, sideChanged)
    }

    @Test
    fun sideCardSvgFileImportInvalid() {
        try {
            getDialogBagAndroidViewModel().cardSideAndroidViewModel.sideImageSvgImport(
                javaClass.classLoader?.getResourceAsStream("data/svg/empty.svg")
            )
            fail()
        } catch (_: CardSideSvgImportException) {
            // expected
        }
    }

    @Test
    fun sideCardSvgFileImportValid() {
        getDialogBagAndroidViewModel().cardSideAndroidViewModel.sideImageSvgImport(
            javaClass.classLoader?.getResourceAsStream("data/svg/story/lion.svg"),
            kiloBytes = 500
        )
    }

    @Test
    fun sideCardSvgClear() {
        val diceInDialogBag = BagDataTestDouble().d6
        val sideInDialogBag = diceInDialogBag.sides[0]
        sideInDialogBag.imageBase64 =
            "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjxzdmcKICAgaGVpZ2h0PSIyNCIKICAgaWQ9InN2ZzQiCiAgIHZlcnNpb249IjEuMSIKICAgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiCiAgIHdpZHRoPSIyNCIKICAgaW5rc2NhcGU6dmVyc2lvbj0iMS4yLjIgKGIwYTg0ODY1NDEsIDIwMjItMTItMDEpIgogICBzb2RpcG9kaTpkb2NuYW1lPSJkNnM2LnN2ZyIKICAgeG1sbnM6aW5rc2NhcGU9Imh0dHA6Ly93d3cuaW5rc2NhcGUub3JnL25hbWVzcGFjZXMvaW5rc2NhcGUiCiAgIHhtbG5zOnNvZGlwb2RpPSJodHRwOi8vc29kaXBvZGkuc291cmNlZm9yZ2UubmV0L0RURC9zb2RpcG9kaS0wLmR0ZCIKICAgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIgogICB4bWxuczpzdmc9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8ZGVmcwogICAgIGlkPSJkZWZzOCIgLz4KICA8c29kaXBvZGk6bmFtZWR2aWV3CiAgICAgYm9yZGVyY29sb3I9IiM2NjY2NjYiCiAgICAgYm9yZGVyb3BhY2l0eT0iMS4wIgogICAgIGlkPSJuYW1lZHZpZXc2IgogICAgIHBhZ2Vjb2xvcj0iI2ZmZmZmZiIKICAgICBzaG93Z3JpZD0iZmFsc2UiCiAgICAgaW5rc2NhcGU6Y3VycmVudC1sYXllcj0ic3ZnNCIKICAgICBpbmtzY2FwZTpjeD0iMTEuNzEzMTM0IgogICAgIGlua3NjYXBlOmN5PSIxNS4wMjM4NjMiCiAgICAgaW5rc2NhcGU6cGFnZWNoZWNrZXJib2FyZD0iMCIKICAgICBpbmtzY2FwZTpwYWdlb3BhY2l0eT0iMC4wIgogICAgIGlua3NjYXBlOnBhZ2VzaGFkb3c9IjIiCiAgICAgaW5rc2NhcGU6d2luZG93LWhlaWdodD0iMTAwOCIKICAgICBpbmtzY2FwZTp3aW5kb3ctbWF4aW1pemVkPSIxIgogICAgIGlua3NjYXBlOndpbmRvdy13aWR0aD0iMTkyMCIKICAgICBpbmtzY2FwZTp3aW5kb3cteD0iMCIKICAgICBpbmtzY2FwZTp3aW5kb3cteT0iMCIKICAgICBpbmtzY2FwZTp6b29tPSIyMS44OTg0OTUiCiAgICAgaW5rc2NhcGU6c2hvd3BhZ2VzaGFkb3c9IjIiCiAgICAgaW5rc2NhcGU6ZGVza2NvbG9yPSIjZDFkMWQxIiAvPgogIDxwYXRoCiAgICAgZD0ibSAzMDAsLTI0MCBjIDE2LjY2NjY3LDAgMzAuODMzMzMsLTUuODMzMzMgNDIuNSwtMTcuNSAxMS42NjY2NywtMTEuNjY2NjcgMTcuNSwtMjUuODMzMzMgMTcuNSwtNDIuNSAwLC0xNi42NjY2NyAtNS44MzMzMywtMzAuODMzMzMgLTE3LjUsLTQyLjUgLTExLjY2NjY3LC0xMS42NjY2NyAtMjUuODMzMzMsLTE3LjUgLTQyLjUsLTE3LjUgLTE2LjY2NjY3LDAgLTMwLjgzMzMzLDUuODMzMzMgLTQyLjUsMTcuNSAtMTEuNjY2NjcsMTEuNjY2NjcgLTE3LjUsMjUuODMzMzMgLTE3LjUsNDIuNSAwLDE2LjY2NjY3IDUuODMzMzMsMzAuODMzMzMgMTcuNSw0Mi41IDExLjY2NjY3LDExLjY2NjY3IDI1LjgzMzMzLDE3LjUgNDIuNSwxNy41IHogbSAwLC0zNjAgYyAxNi42NjY2NywwIDMwLjgzMzMzLC01LjgzMzMzIDQyLjUsLTE3LjUgMTEuNjY2NjcsLTExLjY2NjY3IDE3LjUsLTI1LjgzMzMzIDE3LjUsLTQyLjUgMCwtMTYuNjY2NjcgLTUuODMzMzMsLTMwLjgzMzMzIC0xNy41LC00Mi41IC0xMS42NjY2NywtMTEuNjY2NjcgLTI1LjgzMzMzLC0xNy41IC00Mi41LC0xNy41IC0xNi42NjY2NywwIC0zMC44MzMzMyw1LjgzMzMzIC00Mi41LDE3LjUgLTExLjY2NjY3LDExLjY2NjY3IC0xNy41LDI1LjgzMzMzIC0xNy41LDQyLjUgMCwxNi42NjY2NyA1LjgzMzMzLDMwLjgzMzMzIDE3LjUsNDIuNSAxMS42NjY2NywxMS42NjY2NyAyNS44MzMzMywxNy41IDQyLjUsMTcuNSB6IG0gMzYwLDM2MCBjIDE2LjY2NjY3LDAgMzAuODMzMzMsLTUuODMzMzMgNDIuNSwtMTcuNSAxMS42NjY2NywtMTEuNjY2NjcgMTcuNSwtMjUuODMzMzMgMTcuNSwtNDIuNSAwLC0xNi42NjY2NyAtNS44MzMzMywtMzAuODMzMzMgLTE3LjUsLTQyLjUgLTExLjY2NjY3LC0xMS42NjY2NyAtMjUuODMzMzMsLTE3LjUgLTQyLjUsLTE3LjUgLTE2LjY2NjY3LDAgLTMwLjgzMzMzLDUuODMzMzMgLTQyLjUsMTcuNSAtMTEuNjY2NjcsMTEuNjY2NjcgLTE3LjUsMjUuODMzMzMgLTE3LjUsNDIuNSAwLDE2LjY2NjY3IDUuODMzMzMsMzAuODMzMzMgMTcuNSw0Mi41IDExLjY2NjY3LDExLjY2NjY3IDI1LjgzMzMzLDE3LjUgNDIuNSwxNy41IHogbSAwLC0zNjAgYyAxNi42NjY2NywwIDMwLjgzMzMzLC01LjgzMzMzIDQyLjUsLTE3LjUgMTEuNjY2NjcsLTExLjY2NjY3IDE3LjUsLTI1LjgzMzMzIDE3LjUsLTQyLjUgMCwtMTYuNjY2NjcgLTUuODMzMzMsLTMwLjgzMzMzIC0xNy41LC00Mi41IC0xMS42NjY2NywtMTEuNjY2NjcgLTI1LjgzMzMzLC0xNy41IC00Mi41LC0xNy41IC0xNi42NjY2NywwIC0zMC44MzMzMyw1LjgzMzMzIC00Mi41LDE3LjUgLTExLjY2NjY3LDExLjY2NjY3IC0xNy41LDI1LjgzMzMzIC0xNy41LDQyLjUgMCwxNi42NjY2NyA1LjgzMzMzLDMwLjgzMzMzIDE3LjUsNDIuNSAxMS42NjY2NywxMS42NjY2NyAyNS44MzMzMywxNy41IDQyLjUsMTcuNSB6IG0gLTQ2MCw0ODAgYyAtMjIsMCAtNDAuODMzMzMsLTcuODMzMzMgLTU2LjUsLTIzLjUgQyAxMjcuODMzMzMsLTE1OS4xNjY2NyAxMjAsLTE3OCAxMjAsLTIwMCB2IC01NjAgYyAwLC0yMiA3LjgzMzMzLC00MC44MzMzMyAyMy41LC01Ni41IDE1LjY2NjY3LC0xNS42NjY2NyAzNC41LC0yMy41IDU2LjUsLTIzLjUgaCA1NjAgYyAyMiwwIDQwLjgzMzMzLDcuODMzMzMgNTYuNSwyMy41IDE1LjY2NjY3LDE1LjY2NjY3IDIzLjUsMzQuNSAyMy41LDU2LjUgdiA1NjAgYyAwLDIyIC03LjgzMzMzLDQwLjgzMzMzIC0yMy41LDU2LjUgLTE1LjY2NjY3LDE1LjY2NjY3IC0zNC41LDIzLjUgLTU2LjUsMjMuNSB6IG0gMCwtODAgSCA3NjAgViAtNzYwIEggMjAwIFogbSAwLC01NjAgdiA1NjAgeiIKICAgICBpZD0icGF0aDIiCiAgICAgc29kaXBvZGk6bm9kZXR5cGVzPSJzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3NzY2NjY2NjY2MiCiAgICAgc3R5bGU9ImZpbGw6IzdENTI2MDtmaWxsLW9wYWNpdHk6MTtzdHJva2U6IzdENTI2MDtzdHJva2Utb3BhY2l0eToxIiAvPgogIDxwYXRoCiAgICAgc3R5bGU9InN0cm9rZS13aWR0aDo0MDtmaWxsOiM3RDUyNjA7ZmlsbC1vcGFjaXR5OjEiCiAgICAgZD0ibSA2NjAsLTQyMCBjIDE2LjY2NjY4LDAgMzAuODMzMzIsLTUuODMzMzIgNDIuNSwtMTcuNSAxMS42NjY2OCwtMTEuNjY2NjggMTcuNSwtMjUuODMzMzIgMTcuNSwtNDIuNSAwLC0xNi42NjY2OCAtNS44MzMzMiwtMzAuODMzMzIgLTE3LjUsLTQyLjUgLTExLjY2NjY4LC0xMS42NjY2OCAtMjUuODMzMzIsLTE3LjUgLTQyLjUsLTE3LjUgLTE2LjY2NjY4LDAgLTMwLjgzMzMyLDUuODMzMzIgLTQyLjUsMTcuNSAtMTEuNjY2NjgsMTEuNjY2NjggLTE3LjUsMjUuODMzMzIgLTE3LjUsNDIuNSAwLDE2LjY2NjY4IDUuODMzMzIsMzAuODMzMzIgMTcuNSw0Mi41IDExLjY2NjY4LDExLjY2NjY4IDI1LjgzMzMyLDE3LjUgNDIuNSwxNy41IHoiCiAgICAgaWQ9InBhdGgxMTM2IiAvPgogIDxwYXRoCiAgICAgc3R5bGU9InN0cm9rZS13aWR0aDo0MDtmaWxsOiM3RDUyNjA7ZmlsbC1vcGFjaXR5OjEiCiAgICAgZD0ibSAzMDAsLTQyMCBjIDE2LjY2NjY4LDAgMzAuODMzMzIsLTUuODMzMzIgNDIuNSwtMTcuNSAxMS42NjY2OCwtMTEuNjY2NjggMTcuNSwtMjUuODMzMzIgMTcuNSwtNDIuNSAwLC0xNi42NjY2OCAtNS44MzMzMiwtMzAuODMzMzIgLTE3LjUsLTQyLjUgLTExLjY2NjY4LC0xMS42NjY2OCAtMjUuODMzMzIsLTE3LjUgLTQyLjUsLTE3LjUgLTE2LjY2NjY4LDAgLTMwLjgzMzMyLDUuODMzMzIgLTQyLjUsMTcuNSAtMTEuNjY2NjgsMTEuNjY2NjggLTE3LjUsMjUuODMzMzIgLTE3LjUsNDIuNSAwLDE2LjY2NjY4IDUuODMzMzIsMzAuODMzMzIgMTcuNSw0Mi41IDExLjY2NjY4LDExLjY2NjY4IDI1LjgzMzMyLDE3LjUgNDIuNSwxNy41IHoiCiAgICAgaWQ9InBhdGgxMTM2LTYiIC8+Cjwvc3ZnPgo="

        val dialogBagAndroidViewModel = getDialogBagAndroidViewModel(
            diceInDialogBag, sideInDialogBag
        )

        val bagCardSideAndroidViewModel =
            dialogBagAndroidViewModel.cardSideAndroidViewModel

        assertTrue(bagCardSideAndroidViewModel.sideImageAvailable())

        bagCardSideAndroidViewModel.sideImageSvgClear()

        assertFalse(bagCardSideAndroidViewModel.sideImageAvailable())
    }

    private fun assertStateSide(
        dialogBagAndroidViewModel: DialogBagAndroidViewModel,
        side: Side
    ) {
        val stateFlowSide =
            dialogBagAndroidViewModel.cardSideAndroidViewModel.stateFlowCardSide.value

        assertEquals(side.number, stateFlowSide.sideNumber)
        assertEquals(side.numberColour, stateFlowSide.sideNumberColour)
        assertEquals(side.description, stateFlowSide.sideDescription)
        assertEquals(side.descriptionColour, stateFlowSide.sideDescriptionColour)
        assertNotNull(stateFlowSide.sideImageRequest?.data)
        assertEquals(side.imageBase64, stateFlowSide.sideImageBase64)
    }

    @Test
    fun githubIssue216() {
        try {
            getDialogBagAndroidViewModel().cardSideAndroidViewModel.sideImageSvgImport(
                javaClass.classLoader?.getResourceAsStream("github/issue/216/1.svg")
            )
        } catch (_: CardSideSvgImportException) {
            fail()
        }
    }
}
