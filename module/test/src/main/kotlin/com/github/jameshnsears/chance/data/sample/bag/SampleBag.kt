package com.github.jameshnsears.chance.data.sample.bag

import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.Side

class SampleBag {
    companion object {
        private val d2ImageBase64 =
            "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCEtLSBDcmVhdGVkIHdpdGggSW5rc2NhcGUgKGh0dHA6Ly93d3cuaW5rc2NhcGUub3JnLykgLS0+Cgo8c3ZnIHhtbG5zOmlua3NjYXBlPSJodHRwOi8vd3d3Lmlua3NjYXBlLm9yZy9uYW1lc3BhY2VzL2lua3NjYXBlIgogICAgeG1sbnM6c29kaXBvZGk9Imh0dHA6Ly9zb2RpcG9kaS5zb3VyY2Vmb3JnZS5uZXQvRFREL3NvZGlwb2RpLTAuZHRkIiBoZWlnaHQ9IjEwMG1tIiBpZD0ic3ZnMjM5IgogICAgdmVyc2lvbj0iMS4xIiB2aWV3Qm94PSIwIDAgMTAwIDEwMCIgd2lkdGg9IjEwMG1tIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciCiAgICBpbmtzY2FwZTp2ZXJzaW9uPSIxLjEuMiAoMGEwMGNmNTMzOSwgMjAyMi0wMi0wNCkiIHNvZGlwb2RpOmRvY25hbWU9ImQyLnN2ZyI+CiAgICA8c29kaXBvZGk6bmFtZWR2aWV3IGJvcmRlcmNvbG9yPSIjMDAwMDAwIiBib3JkZXJvcGFjaXR5PSIwLjI1IiBpZD0ibmFtZWR2aWV3MjQxIgogICAgICAgIHBhZ2Vjb2xvcj0iI2ZmZmZmZiIgc2hvd2dyaWQ9ImZhbHNlIiBpbmtzY2FwZTpjdXJyZW50LWxheWVyPSJsYXllcjEiIGlua3NjYXBlOmN4PSI0Mi40MTMyOCIKICAgICAgICBpbmtzY2FwZTpjeT0iMTk0LjMxMiIgaW5rc2NhcGU6ZGVza2NvbG9yPSIjZDFkMWQxIiBpbmtzY2FwZTpkb2N1bWVudC11bml0cz0ibW0iCiAgICAgICAgaW5rc2NhcGU6cGFnZWNoZWNrZXJib2FyZD0iMCIgaW5rc2NhcGU6cGFnZW9wYWNpdHk9IjAuMCIgaW5rc2NhcGU6cGFnZXNoYWRvdz0iMiIKICAgICAgICBpbmtzY2FwZTpzaG93cGFnZXNoYWRvdz0iMiIgaW5rc2NhcGU6d2luZG93LWhlaWdodD0iNzM2IiBpbmtzY2FwZTp3aW5kb3ctbWF4aW1pemVkPSIxIgogICAgICAgIGlua3NjYXBlOndpbmRvdy13aWR0aD0iMTM2NiIgaW5rc2NhcGU6d2luZG93LXg9IjAiIGlua3NjYXBlOndpbmRvdy15PSIwIgogICAgICAgIGlua3NjYXBlOnpvb209IjEuMDEzODMzNCIgLz4KICAgIDxkZWZzIGlkPSJkZWZzMjM2Ij4KICAgICAgICA8Y2xpcFBhdGggY2xpcFBhdGhVbml0cz0idXNlclNwYWNlT25Vc2UiIGlkPSJwcmVzZW50YXRpb25fY2xpcF9wYXRoIj4KICAgICAgICAgICAgPHJlY3QgaGVpZ2h0PSIxNDU0MCIgaWQ9InJlY3Q0OTQiIHdpZHRoPSIxNDUzNCIgeD0iMCIgeT0iMCIgLz4KICAgICAgICA8L2NsaXBQYXRoPgogICAgPC9kZWZzPgogICAgPGcgc3R5bGU9ImRpc3BsYXk6aW5saW5lIiBpZD0ibGF5ZXIxIgogICAgICAgIHRyYW5zZm9ybT0ibWF0cml4KDAuNjMzMDI1MDgsMCwwLDAuNjMyOTgwOTksLTguMTc3NTIxNSwtMy42NjQ4NDc3KSIKICAgICAgICBpbmtzY2FwZTpncm91cG1vZGU9ImxheWVyIiBpbmtzY2FwZTpsYWJlbD0iZDIiPgogICAgICAgIDxwYXRoCiAgICAgICAgICAgIHN0eWxlPSJvcGFjaXR5OjE7ZmlsbDojMDAwMDAwO2ZpbGwtb3BhY2l0eTowLjk3NDc3MDYxO3N0cm9rZTojMDAwMDAwO3N0cm9rZS13aWR0aDozLjE1OTU0O3N0cm9rZS1kYXNoYXJyYXk6bm9uZTtzdHJva2Utb3BhY2l0eToxO2ZpbGwtcnVsZTpub256ZXJvIgogICAgICAgICAgICBjbGFzcz0ic3QwIgogICAgICAgICAgICBkPSJtIDE2MC45NTA0OCwxMDkuOTEyMDEgYyAtMTMuODQ3NTUsMzguMTI2OCAtNTYuMDIwODYsNTcuNzgxNDEgLTk0LjE0NzYzMiw0My45MDc1NiBDIDI4LjY0OTgwNiwxMzkuOTQ1NzMgOS4wMjE0ODQ2LDk3Ljc3MjM5NSAyMi44OTUzMTksNTkuNjcxODc1IDM2Ljc2OTE1NSwyMS41NDUwNzggNzguOTE2MTc1LDEuODkwNDYzIDExNy4wNDI5NSwxNS43NjQzMDggYyAzOC4xMDA0OSwxMy44NDc1NjggNTcuNzU1MDksNTYuMDIwOTAzIDQzLjkwNzUzLDk0LjE0NzcwMiB6IgogICAgICAgICAgICBpZD0iQ2lyY2xlXzJfIiBpbmtzY2FwZTpjb25uZWN0b3ItY3VydmF0dXJlPSIwIiBpbmtzY2FwZTpsYWJlbD0iZDIiIC8+CiAgICA8L2c+Cjwvc3ZnPgo="
        val d2 = Dice(
            sides = listOf(
                Side(
                    number = 2,
                    imageBase64 = d2ImageBase64,
                    description = "d2 Side # 2"
                ),
                Side(
                    number = 1,
                    imageBase64 = d2ImageBase64,
                    description = "d2 Side # 1"
                ),
            ),
            titleStringsId = R.string.d2,
            selected = true,
            multiplier = true,
        )

        private val d4ImageBase64 =
            "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+PCEtLSBDcmVhdGVkIHdpdGggSW5rc2NhcGUgKGh0dHA6Ly93d3cuaW5rc2NhcGUub3JnLykgLS0+Cgo8c3ZnIHhtbG5zOmlua3NjYXBlPSJodHRwOi8vd3d3Lmlua3NjYXBlLm9yZy9uYW1lc3BhY2VzL2lua3NjYXBlIgogICAgeG1sbnM6c29kaXBvZGk9Imh0dHA6Ly9zb2RpcG9kaS5zb3VyY2Vmb3JnZS5uZXQvRFREL3NvZGlwb2RpLTAuZHRkIiBoZWlnaHQ9IjEwMG1tIiBpZD0ic3ZnMjM5IgogICAgdmVyc2lvbj0iMS4xIiB2aWV3Qm94PSIwIDAgMTAwIDEwMCIgd2lkdGg9IjEwMG1tIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciCiAgICBpbmtzY2FwZTp2ZXJzaW9uPSIxLjEuMiAoMGEwMGNmNTMzOSwgMjAyMi0wMi0wNCkiIHNvZGlwb2RpOmRvY25hbWU9ImQ0X2Q4X2QyMC5zdmciPgogICAgPHNvZGlwb2RpOm5hbWVkdmlldyBib3JkZXJjb2xvcj0iIzAwMDAwMCIgYm9yZGVyb3BhY2l0eT0iMC4yNSIgaWQ9Im5hbWVkdmlldzI0MSIKICAgICAgICBwYWdlY29sb3I9IiNmZmZmZmYiIHNob3dncmlkPSJmYWxzZSIgaW5rc2NhcGU6Y3VycmVudC1sYXllcj0ibGF5ZXIyIiBpbmtzY2FwZTpjeD0iMTkxLjQxNDYiCiAgICAgICAgaW5rc2NhcGU6Y3k9IjE4MC4zMjg4MiIgaW5rc2NhcGU6ZGVza2NvbG9yPSIjZDFkMWQxIiBpbmtzY2FwZTpkb2N1bWVudC11bml0cz0ibW0iCiAgICAgICAgaW5rc2NhcGU6cGFnZWNoZWNrZXJib2FyZD0iMCIgaW5rc2NhcGU6cGFnZW9wYWNpdHk9IjAuMCIgaW5rc2NhcGU6cGFnZXNoYWRvdz0iMiIKICAgICAgICBpbmtzY2FwZTpzaG93cGFnZXNoYWRvdz0iMiIgaW5rc2NhcGU6d2luZG93LWhlaWdodD0iNzM2IiBpbmtzY2FwZTp3aW5kb3ctbWF4aW1pemVkPSIxIgogICAgICAgIGlua3NjYXBlOndpbmRvdy13aWR0aD0iMTM2NiIgaW5rc2NhcGU6d2luZG93LXg9IjAiIGlua3NjYXBlOndpbmRvdy15PSIwIgogICAgICAgIGlua3NjYXBlOnpvb209IjEuMzUzMDgzOCIgLz4KICAgIDxkZWZzIGlkPSJkZWZzMjM2Ij4KICAgICAgICA8aW5rc2NhcGU6cGVyc3BlY3RpdmUgaWQ9InBlcnNwZWN0aXZlODkzIiBpbmtzY2FwZTpwZXJzcDNkLW9yaWdpbj0iNTAgOiAzMy4zMzMzMzMgOiAxIgogICAgICAgICAgICBpbmtzY2FwZTp2cF94PSIwIDogNTAgOiAxIiBpbmtzY2FwZTp2cF95PSIwIDogMTAwMCA6IDAiIGlua3NjYXBlOnZwX3o9IjEwMCA6IDUwIDogMSIKICAgICAgICAgICAgc29kaXBvZGk6dHlwZT0iaW5rc2NhcGU6cGVyc3AzZCIgLz4KICAgICAgICA8Y2xpcFBhdGggY2xpcFBhdGhVbml0cz0idXNlclNwYWNlT25Vc2UiIGlkPSJwcmVzZW50YXRpb25fY2xpcF9wYXRoIj4KICAgICAgICAgICAgPHJlY3QgaGVpZ2h0PSIxNDU0MCIgaWQ9InJlY3Q0OTQiIHdpZHRoPSIxNDUzNCIgeD0iMCIgeT0iMCIgLz4KICAgICAgICA8L2NsaXBQYXRoPgogICAgPC9kZWZzPgogICAgPGcgc3R5bGU9ImRpc3BsYXk6aW5saW5lO29wYWNpdHk6MSIgaWQ9ImxheWVyMiIgaW5rc2NhcGU6Z3JvdXBtb2RlPSJsYXllciIKICAgICAgICBpbmtzY2FwZTpsYWJlbD0iZDRfZDhfZDIwIj4KICAgICAgICA8cGF0aAogICAgICAgICAgICBzdHlsZT0iZGlzcGxheTppbmxpbmU7ZmlsbDojMDAwMDAwO2ZpbGwtb3BhY2l0eToxO3N0cm9rZS13aWR0aDoyO3N0cm9rZS1kYXNoYXJyYXk6bm9uZSIKICAgICAgICAgICAgZD0iTSA1MC4wNTkwMTIsNC42NDQyODE4IDk3LjQwMTk4Nyw5Ni40MTYwOTQgSCAyLjcxNTk4OTEgWiIgZmlsbD0ibm9uZSIgaWQ9InBhdGgyODYiCiAgICAgICAgICAgIHN0cm9rZT0iIzAwMDAwMCIgc3Ryb2tlLXdpZHRoPSIxLjcxNjUyIiBpbmtzY2FwZTpsYWJlbD0iZDRfZDhfZDIwIiAvPgogICAgPC9nPgo8L3N2Zz4K"
        val d4 = Dice(
            sides = listOf(
                Side(
                    number = 4,
                    imageBase64 = d4ImageBase64
                ),
                Side(
                    number = 3,
                    imageBase64 = d4ImageBase64
                ),
                Side(
                    number = 2,
                    imageBase64 = d4ImageBase64
                ),
                Side(
                    number = 1,
                    imageBase64 = d4ImageBase64
                ),
            ),
            titleStringsId = R.string.d4,
            selected = true,
            multiplier = true,
            explode = true,
        )

        val d6 = Dice(
            sides = (6 downTo 1).map { index -> Side(number = index) },
            title = "d6",
            selected = true,
            multiplier = true,
            explode = true,
            modifyScore = true
        )

        val d8 = Dice(
            sides = (8 downTo 1).map { index -> Side(number = index) },
            title = "d8",
        )

        val d10 = Dice(
            sides = (10 downTo 1).map { index -> Side(number = index) },
            title = "d10",
        )

        val d12 = Dice(
            sides = (12 downTo 1).map { index -> Side(number = index) },
            title = "d12",
        )

        val d20 = Dice(
            sides = (20 downTo 1).map { index -> Side(number = index) },
            title = "d20",
        )

        val allDice = mutableListOf(
            d2,
            d4,
            d6,
            d8,
            d10,
            d12,
            d20,
        )
    }
}
