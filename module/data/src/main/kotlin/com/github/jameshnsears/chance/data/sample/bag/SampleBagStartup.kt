package com.github.jameshnsears.chance.data.sample.bag

import com.github.jameshnsears.chance.data.R
import com.github.jameshnsears.chance.data.domain.state.Dice
import com.github.jameshnsears.chance.data.domain.state.Side

class SampleBagStartup {
    val d6 = Dice(
        sides = listOf(
            Side(
                number = 6,
                imageBase64 = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjxzdmcKICAgaGVpZ2h0PSIyNCIKICAgaWQ9InN2ZzQiCiAgIHZlcnNpb249IjEuMSIKICAgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiCiAgIHdpZHRoPSIyNCIKICAgaW5rc2NhcGU6dmVyc2lvbj0iMS4yLjIgKGIwYTg0ODY1NDEsIDIwMjItMTItMDEpIgogICBzb2RpcG9kaTpkb2NuYW1lPSJkNnM2LnN2ZyIKICAgeG1sbnM6aW5rc2NhcGU9Imh0dHA6Ly93d3cuaW5rc2NhcGUub3JnL25hbWVzcGFjZXMvaW5rc2NhcGUiCiAgIHhtbG5zOnNvZGlwb2RpPSJodHRwOi8vc29kaXBvZGkuc291cmNlZm9yZ2UubmV0L0RURC9zb2RpcG9kaS0wLmR0ZCIKICAgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIgogICB4bWxuczpzdmc9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8ZGVmcwogICAgIGlkPSJkZWZzOCIgLz4KICA8c29kaXBvZGk6bmFtZWR2aWV3CiAgICAgYm9yZGVyY29sb3I9IiM2NjY2NjYiCiAgICAgYm9yZGVyb3BhY2l0eT0iMS4wIgogICAgIGlkPSJuYW1lZHZpZXc2IgogICAgIHBhZ2Vjb2xvcj0iI2ZmZmZmZiIKICAgICBzaG93Z3JpZD0iZmFsc2UiCiAgICAgaW5rc2NhcGU6Y3VycmVudC1sYXllcj0ic3ZnNCIKICAgICBpbmtzY2FwZTpjeD0iMTEuNzEzMTM0IgogICAgIGlua3NjYXBlOmN5PSIxNS4wMjM4NjMiCiAgICAgaW5rc2NhcGU6cGFnZWNoZWNrZXJib2FyZD0iMCIKICAgICBpbmtzY2FwZTpwYWdlb3BhY2l0eT0iMC4wIgogICAgIGlua3NjYXBlOnBhZ2VzaGFkb3c9IjIiCiAgICAgaW5rc2NhcGU6d2luZG93LWhlaWdodD0iMTAwOCIKICAgICBpbmtzY2FwZTp3aW5kb3ctbWF4aW1pemVkPSIxIgogICAgIGlua3NjYXBlOndpbmRvdy13aWR0aD0iMTkyMCIKICAgICBpbmtzY2FwZTp3aW5kb3cteD0iMCIKICAgICBpbmtzY2FwZTp3aW5kb3cteT0iMCIKICAgICBpbmtzY2FwZTp6b29tPSIyMS44OTg0OTUiCiAgICAgaW5rc2NhcGU6c2hvd3BhZ2VzaGFkb3c9IjIiCiAgICAgaW5rc2NhcGU6ZGVza2NvbG9yPSIjZDFkMWQxIiAvPgogIDxwYXRoCiAgICAgZD0ibSAzMDAsLTI0MCBjIDE2LjY2NjY3LDAgMzAuODMzMzMsLTUuODMzMzMgNDIuNSwtMTcuNSAxMS42NjY2NywtMTEuNjY2NjcgMTcuNSwtMjUuODMzMzMgMTcuNSwtNDIuNSAwLC0xNi42NjY2NyAtNS44MzMzMywtMzAuODMzMzMgLTE3LjUsLTQyLjUgLTExLjY2NjY3LC0xMS42NjY2NyAtMjUuODMzMzMsLTE3LjUgLTQyLjUsLTE3LjUgLTE2LjY2NjY3LDAgLTMwLjgzMzMzLDUuODMzMzMgLTQyLjUsMTcuNSAtMTEuNjY2NjcsMTEuNjY2NjcgLTE3LjUsMjUuODMzMzMgLTE3LjUsNDIuNSAwLDE2LjY2NjY3IDUuODMzMzMsMzAuODMzMzMgMTcuNSw0Mi41IDExLjY2NjY3LDExLjY2NjY3IDI1LjgzMzMzLDE3LjUgNDIuNSwxNy41IHogbSAwLC0zNjAgYyAxNi42NjY2NywwIDMwLjgzMzMzLC01LjgzMzMzIDQyLjUsLTE3LjUgMTEuNjY2NjcsLTExLjY2NjY3IDE3LjUsLTI1LjgzMzMzIDE3LjUsLTQyLjUgMCwtMTYuNjY2NjcgLTUuODMzMzMsLTMwLjgzMzMzIC0xNy41LC00Mi41IC0xMS42NjY2NywtMTEuNjY2NjcgLTI1LjgzMzMzLC0xNy41IC00Mi41LC0xNy41IC0xNi42NjY2NywwIC0zMC44MzMzMyw1LjgzMzMzIC00Mi41LDE3LjUgLTExLjY2NjY3LDExLjY2NjY3IC0xNy41LDI1LjgzMzMzIC0xNy41LDQyLjUgMCwxNi42NjY2NyA1LjgzMzMzLDMwLjgzMzMzIDE3LjUsNDIuNSAxMS42NjY2NywxMS42NjY2NyAyNS44MzMzMywxNy41IDQyLjUsMTcuNSB6IG0gMzYwLDM2MCBjIDE2LjY2NjY3LDAgMzAuODMzMzMsLTUuODMzMzMgNDIuNSwtMTcuNSAxMS42NjY2NywtMTEuNjY2NjcgMTcuNSwtMjUuODMzMzMgMTcuNSwtNDIuNSAwLC0xNi42NjY2NyAtNS44MzMzMywtMzAuODMzMzMgLTE3LjUsLTQyLjUgLTExLjY2NjY3LC0xMS42NjY2NyAtMjUuODMzMzMsLTE3LjUgLTQyLjUsLTE3LjUgLTE2LjY2NjY3LDAgLTMwLjgzMzMzLDUuODMzMzMgLTQyLjUsMTcuNSAtMTEuNjY2NjcsMTEuNjY2NjcgLTE3LjUsMjUuODMzMzMgLTE3LjUsNDIuNSAwLDE2LjY2NjY3IDUuODMzMzMsMzAuODMzMzMgMTcuNSw0Mi41IDExLjY2NjY3LDExLjY2NjY3IDI1LjgzMzMzLDE3LjUgNDIuNSwxNy41IHogbSAwLC0zNjAgYyAxNi42NjY2NywwIDMwLjgzMzMzLC01LjgzMzMzIDQyLjUsLTE3LjUgMTEuNjY2NjcsLTExLjY2NjY3IDE3LjUsLTI1LjgzMzMzIDE3LjUsLTQyLjUgMCwtMTYuNjY2NjcgLTUuODMzMzMsLTMwLjgzMzMzIC0xNy41LC00Mi41IC0xMS42NjY2NywtMTEuNjY2NjcgLTI1LjgzMzMzLC0xNy41IC00Mi41LC0xNy41IC0xNi42NjY2NywwIC0zMC44MzMzMyw1LjgzMzMzIC00Mi41LDE3LjUgLTExLjY2NjY3LDExLjY2NjY3IC0xNy41LDI1LjgzMzMzIC0xNy41LDQyLjUgMCwxNi42NjY2NyA1LjgzMzMzLDMwLjgzMzMzIDE3LjUsNDIuNSAxMS42NjY2NywxMS42NjY2NyAyNS44MzMzMywxNy41IDQyLjUsMTcuNSB6IG0gLTQ2MCw0ODAgYyAtMjIsMCAtNDAuODMzMzMsLTcuODMzMzMgLTU2LjUsLTIzLjUgQyAxMjcuODMzMzMsLTE1OS4xNjY2NyAxMjAsLTE3OCAxMjAsLTIwMCB2IC01NjAgYyAwLC0yMiA3LjgzMzMzLC00MC44MzMzMyAyMy41LC01Ni41IDE1LjY2NjY3LC0xNS42NjY2NyAzNC41LC0yMy41IDU2LjUsLTIzLjUgaCA1NjAgYyAyMiwwIDQwLjgzMzMzLDcuODMzMzMgNTYuNSwyMy41IDE1LjY2NjY3LDE1LjY2NjY3IDIzLjUsMzQuNSAyMy41LDU2LjUgdiA1NjAgYyAwLDIyIC03LjgzMzMzLDQwLjgzMzMzIC0yMy41LDU2LjUgLTE1LjY2NjY3LDE1LjY2NjY3IC0zNC41LDIzLjUgLTU2LjUsMjMuNSB6IG0gMCwtODAgSCA3NjAgViAtNzYwIEggMjAwIFogbSAwLC01NjAgdiA1NjAgeiIKICAgICBpZD0icGF0aDIiCiAgICAgc29kaXBvZGk6bm9kZXR5cGVzPSJzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3NzY2NjY2NjY2MiCiAgICAgc3R5bGU9ImZpbGw6IzdENTI2MDtmaWxsLW9wYWNpdHk6MTtzdHJva2U6IzdENTI2MDtzdHJva2Utb3BhY2l0eToxIiAvPgogIDxwYXRoCiAgICAgc3R5bGU9InN0cm9rZS13aWR0aDo0MDtmaWxsOiM3RDUyNjA7ZmlsbC1vcGFjaXR5OjEiCiAgICAgZD0ibSA2NjAsLTQyMCBjIDE2LjY2NjY4LDAgMzAuODMzMzIsLTUuODMzMzIgNDIuNSwtMTcuNSAxMS42NjY2OCwtMTEuNjY2NjggMTcuNSwtMjUuODMzMzIgMTcuNSwtNDIuNSAwLC0xNi42NjY2OCAtNS44MzMzMiwtMzAuODMzMzIgLTE3LjUsLTQyLjUgLTExLjY2NjY4LC0xMS42NjY2OCAtMjUuODMzMzIsLTE3LjUgLTQyLjUsLTE3LjUgLTE2LjY2NjY4LDAgLTMwLjgzMzMyLDUuODMzMzIgLTQyLjUsMTcuNSAtMTEuNjY2NjgsMTEuNjY2NjggLTE3LjUsMjUuODMzMzIgLTE3LjUsNDIuNSAwLDE2LjY2NjY4IDUuODMzMzIsMzAuODMzMzIgMTcuNSw0Mi41IDExLjY2NjY4LDExLjY2NjY4IDI1LjgzMzMyLDE3LjUgNDIuNSwxNy41IHoiCiAgICAgaWQ9InBhdGgxMTM2IiAvPgogIDxwYXRoCiAgICAgc3R5bGU9InN0cm9rZS13aWR0aDo0MDtmaWxsOiM3RDUyNjA7ZmlsbC1vcGFjaXR5OjEiCiAgICAgZD0ibSAzMDAsLTQyMCBjIDE2LjY2NjY4LDAgMzAuODMzMzIsLTUuODMzMzIgNDIuNSwtMTcuNSAxMS42NjY2OCwtMTEuNjY2NjggMTcuNSwtMjUuODMzMzIgMTcuNSwtNDIuNSAwLC0xNi42NjY2OCAtNS44MzMzMiwtMzAuODMzMzIgLTE3LjUsLTQyLjUgLTExLjY2NjY4LC0xMS42NjY2OCAtMjUuODMzMzIsLTE3LjUgLTQyLjUsLTE3LjUgLTE2LjY2NjY4LDAgLTMwLjgzMzMyLDUuODMzMzIgLTQyLjUsMTcuNSAtMTEuNjY2NjgsMTEuNjY2NjggLTE3LjUsMjUuODMzMzIgLTE3LjUsNDIuNSAwLDE2LjY2NjY4IDUuODMzMzIsMzAuODMzMzIgMTcuNSw0Mi41IDExLjY2NjY4LDExLjY2NjY4IDI1LjgzMzMyLDE3LjUgNDIuNSwxNy41IHoiCiAgICAgaWQ9InBhdGgxMTM2LTYiIC8+Cjwvc3ZnPgo=",
                description = "6",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 5,
                imageBase64 = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjxzdmcKICAgaGVpZ2h0PSIyNCIKICAgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiCiAgIHdpZHRoPSIyNCIKICAgdmVyc2lvbj0iMS4xIgogICBpZD0ic3ZnNCIKICAgc29kaXBvZGk6ZG9jbmFtZT0iZDZzNS5zdmciCiAgIGlua3NjYXBlOnZlcnNpb249IjEuMi4yIChiMGE4NDg2NTQxLCAyMDIyLTEyLTAxKSIKICAgeG1sbnM6aW5rc2NhcGU9Imh0dHA6Ly93d3cuaW5rc2NhcGUub3JnL25hbWVzcGFjZXMvaW5rc2NhcGUiCiAgIHhtbG5zOnNvZGlwb2RpPSJodHRwOi8vc29kaXBvZGkuc291cmNlZm9yZ2UubmV0L0RURC9zb2RpcG9kaS0wLmR0ZCIKICAgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIgogICB4bWxuczpzdmc9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8ZGVmcwogICAgIGlkPSJkZWZzOCIgLz4KICA8c29kaXBvZGk6bmFtZWR2aWV3CiAgICAgaWQ9Im5hbWVkdmlldzYiCiAgICAgcGFnZWNvbG9yPSIjZmZmZmZmIgogICAgIGJvcmRlcmNvbG9yPSIjMDAwMDAwIgogICAgIGJvcmRlcm9wYWNpdHk9IjAuMjUiCiAgICAgaW5rc2NhcGU6c2hvd3BhZ2VzaGFkb3c9IjIiCiAgICAgaW5rc2NhcGU6cGFnZW9wYWNpdHk9IjAuMCIKICAgICBpbmtzY2FwZTpwYWdlY2hlY2tlcmJvYXJkPSIwIgogICAgIGlua3NjYXBlOmRlc2tjb2xvcj0iI2QxZDFkMSIKICAgICBzaG93Z3JpZD0iZmFsc2UiCiAgICAgaW5rc2NhcGU6em9vbT0iMzQuOTE2NjY3IgogICAgIGlua3NjYXBlOmN4PSIxMi4wMTQzMiIKICAgICBpbmtzY2FwZTpjeT0iMTIiCiAgICAgaW5rc2NhcGU6d2luZG93LXdpZHRoPSIxOTIwIgogICAgIGlua3NjYXBlOndpbmRvdy1oZWlnaHQ9IjEwMDgiCiAgICAgaW5rc2NhcGU6d2luZG93LXg9IjAiCiAgICAgaW5rc2NhcGU6d2luZG93LXk9IjAiCiAgICAgaW5rc2NhcGU6d2luZG93LW1heGltaXplZD0iMSIKICAgICBpbmtzY2FwZTpjdXJyZW50LWxheWVyPSJzdmc0IiAvPgogIDxwYXRoCiAgICAgZD0iTTMwMC0yNDBxMjUgMCA0Mi41LTE3LjVUMzYwLTMwMHEwLTI1LTE3LjUtNDIuNVQzMDAtMzYwcS0yNSAwLTQyLjUgMTcuNVQyNDAtMzAwcTAgMjUgMTcuNSA0Mi41VDMwMC0yNDBabTAtMzYwcTI1IDAgNDIuNS0xNy41VDM2MC02NjBxMC0yNS0xNy41LTQyLjVUMzAwLTcyMHEtMjUgMC00Mi41IDE3LjVUMjQwLTY2MHEwIDI1IDE3LjUgNDIuNVQzMDAtNjAwWm0xODAgMTgwcTI1IDAgNDIuNS0xNy41VDU0MC00ODBxMC0yNS0xNy41LTQyLjVUNDgwLTU0MHEtMjUgMC00Mi41IDE3LjVUNDIwLTQ4MHEwIDI1IDE3LjUgNDIuNVQ0ODAtNDIwWm0xODAgMTgwcTI1IDAgNDIuNS0xNy41VDcyMC0zMDBxMC0yNS0xNy41LTQyLjVUNjYwLTM2MHEtMjUgMC00Mi41IDE3LjVUNjAwLTMwMHEwIDI1IDE3LjUgNDIuNVQ2NjAtMjQwWm0wLTM2MHEyNSAwIDQyLjUtMTcuNVQ3MjAtNjYwcTAtMjUtMTcuNS00Mi41VDY2MC03MjBxLTI1IDAtNDIuNSAxNy41VDYwMC02NjBxMCAyNSAxNy41IDQyLjVUNjYwLTYwMFpNMjAwLTEyMHEtMzMgMC01Ni41LTIzLjVUMTIwLTIwMHYtNTYwcTAtMzMgMjMuNS01Ni41VDIwMC04NDBoNTYwcTMzIDAgNTYuNSAyMy41VDg0MC03NjB2NTYwcTAgMzMtMjMuNSA1Ni41VDc2MC0xMjBIMjAwWm0wLTgwaDU2MHYtNTYwSDIwMHY1NjBabTAtNTYwdjU2MC01NjBaIgogICAgIGlkPSJwYXRoMiIKICAgICBzdHlsZT0iZmlsbDojN0Q1MjYwO2ZpbGwtb3BhY2l0eToxO3N0cm9rZTojN0Q1MjYwO3N0cm9rZS1vcGFjaXR5OjEiIC8+Cjwvc3ZnPgo=",
                description = "5",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 4,
                imageBase64 = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjxzdmcKICAgaGVpZ2h0PSIyNCIKICAgaWQ9InN2ZzQiCiAgIHZlcnNpb249IjEuMSIKICAgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiCiAgIHdpZHRoPSIyNCIKICAgaW5rc2NhcGU6dmVyc2lvbj0iMS4yLjIgKGIwYTg0ODY1NDEsIDIwMjItMTItMDEpIgogICBzb2RpcG9kaTpkb2NuYW1lPSJkNnM0LnN2ZyIKICAgeG1sbnM6aW5rc2NhcGU9Imh0dHA6Ly93d3cuaW5rc2NhcGUub3JnL25hbWVzcGFjZXMvaW5rc2NhcGUiCiAgIHhtbG5zOnNvZGlwb2RpPSJodHRwOi8vc29kaXBvZGkuc291cmNlZm9yZ2UubmV0L0RURC9zb2RpcG9kaS0wLmR0ZCIKICAgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIgogICB4bWxuczpzdmc9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8ZGVmcwogICAgIGlkPSJkZWZzOCIgLz4KICA8c29kaXBvZGk6bmFtZWR2aWV3CiAgICAgYm9yZGVyY29sb3I9IiM2NjY2NjYiCiAgICAgYm9yZGVyb3BhY2l0eT0iMS4wIgogICAgIGlkPSJuYW1lZHZpZXc2IgogICAgIHBhZ2Vjb2xvcj0iI2ZmZmZmZiIKICAgICBzaG93Z3JpZD0iZmFsc2UiCiAgICAgaW5rc2NhcGU6Y3VycmVudC1sYXllcj0ic3ZnNCIKICAgICBpbmtzY2FwZTpjeD0iLTMuMzI3NjgxNCIKICAgICBpbmtzY2FwZTpjeT0iMTEuNjYyMTUiCiAgICAgaW5rc2NhcGU6cGFnZWNoZWNrZXJib2FyZD0iMCIKICAgICBpbmtzY2FwZTpwYWdlb3BhY2l0eT0iMC4wIgogICAgIGlua3NjYXBlOnBhZ2VzaGFkb3c9IjIiCiAgICAgaW5rc2NhcGU6d2luZG93LWhlaWdodD0iMTAwOCIKICAgICBpbmtzY2FwZTp3aW5kb3ctbWF4aW1pemVkPSIxIgogICAgIGlua3NjYXBlOndpbmRvdy13aWR0aD0iMTkyMCIKICAgICBpbmtzY2FwZTp3aW5kb3cteD0iMCIKICAgICBpbmtzY2FwZTp3aW5kb3cteT0iMCIKICAgICBpbmtzY2FwZTp6b29tPSIxNi4zNzc3NyIKICAgICBpbmtzY2FwZTpzaG93cGFnZXNoYWRvdz0iMiIKICAgICBpbmtzY2FwZTpkZXNrY29sb3I9IiNkMWQxZDEiIC8+CiAgPHBhdGgKICAgICBkPSJtIDMwMCwtMjQwIGMgMTYuNjY2NjcsMCAzMC44MzMzMywtNS44MzMzMyA0Mi41LC0xNy41IDExLjY2NjY3LC0xMS42NjY2NyAxNy41LC0yNS44MzMzMyAxNy41LC00Mi41IDAsLTE2LjY2NjY3IC01LjgzMzMzLC0zMC44MzMzMyAtMTcuNSwtNDIuNSAtMTEuNjY2NjcsLTExLjY2NjY3IC0yNS44MzMzMywtMTcuNSAtNDIuNSwtMTcuNSAtMTYuNjY2NjcsMCAtMzAuODMzMzMsNS44MzMzMyAtNDIuNSwxNy41IC0xMS42NjY2NywxMS42NjY2NyAtMTcuNSwyNS44MzMzMyAtMTcuNSw0Mi41IDAsMTYuNjY2NjcgNS44MzMzMywzMC44MzMzMyAxNy41LDQyLjUgMTEuNjY2NjcsMTEuNjY2NjcgMjUuODMzMzMsMTcuNSA0Mi41LDE3LjUgeiBtIDAsLTM2MCBjIDE2LjY2NjY3LDAgMzAuODMzMzMsLTUuODMzMzMgNDIuNSwtMTcuNSAxMS42NjY2NywtMTEuNjY2NjcgMTcuNSwtMjUuODMzMzMgMTcuNSwtNDIuNSAwLC0xNi42NjY2NyAtNS44MzMzMywtMzAuODMzMzMgLTE3LjUsLTQyLjUgLTExLjY2NjY3LC0xMS42NjY2NyAtMjUuODMzMzMsLTE3LjUgLTQyLjUsLTE3LjUgLTE2LjY2NjY3LDAgLTMwLjgzMzMzLDUuODMzMzMgLTQyLjUsMTcuNSAtMTEuNjY2NjcsMTEuNjY2NjcgLTE3LjUsMjUuODMzMzMgLTE3LjUsNDIuNSAwLDE2LjY2NjY3IDUuODMzMzMsMzAuODMzMzMgMTcuNSw0Mi41IDExLjY2NjY3LDExLjY2NjY3IDI1LjgzMzMzLDE3LjUgNDIuNSwxNy41IHogbSAzNjAsMzYwIGMgMTYuNjY2NjcsMCAzMC44MzMzMywtNS44MzMzMyA0Mi41LC0xNy41IDExLjY2NjY3LC0xMS42NjY2NyAxNy41LC0yNS44MzMzMyAxNy41LC00Mi41IDAsLTE2LjY2NjY3IC01LjgzMzMzLC0zMC44MzMzMyAtMTcuNSwtNDIuNSAtMTEuNjY2NjcsLTExLjY2NjY3IC0yNS44MzMzMywtMTcuNSAtNDIuNSwtMTcuNSAtMTYuNjY2NjcsMCAtMzAuODMzMzMsNS44MzMzMyAtNDIuNSwxNy41IC0xMS42NjY2NywxMS42NjY2NyAtMTcuNSwyNS44MzMzMyAtMTcuNSw0Mi41IDAsMTYuNjY2NjcgNS44MzMzMywzMC44MzMzMyAxNy41LDQyLjUgMTEuNjY2NjcsMTEuNjY2NjcgMjUuODMzMzMsMTcuNSA0Mi41LDE3LjUgeiBtIDAsLTM2MCBjIDE2LjY2NjY3LDAgMzAuODMzMzMsLTUuODMzMzMgNDIuNSwtMTcuNSAxMS42NjY2NywtMTEuNjY2NjcgMTcuNSwtMjUuODMzMzMgMTcuNSwtNDIuNSAwLC0xNi42NjY2NyAtNS44MzMzMywtMzAuODMzMzMgLTE3LjUsLTQyLjUgLTExLjY2NjY3LC0xMS42NjY2NyAtMjUuODMzMzMsLTE3LjUgLTQyLjUsLTE3LjUgLTE2LjY2NjY3LDAgLTMwLjgzMzMzLDUuODMzMzMgLTQyLjUsMTcuNSAtMTEuNjY2NjcsMTEuNjY2NjcgLTE3LjUsMjUuODMzMzMgLTE3LjUsNDIuNSAwLDE2LjY2NjY3IDUuODMzMzMsMzAuODMzMzMgMTcuNSw0Mi41IDExLjY2NjY3LDExLjY2NjY3IDI1LjgzMzMzLDE3LjUgNDIuNSwxNy41IHogbSAtNDYwLDQ4MCBjIC0yMiwwIC00MC44MzMzMywtNy44MzMzMyAtNTYuNSwtMjMuNSBDIDEyNy44MzMzMywtMTU5LjE2NjY3IDEyMCwtMTc4IDEyMCwtMjAwIHYgLTU2MCBjIDAsLTIyIDcuODMzMzMsLTQwLjgzMzMzIDIzLjUsLTU2LjUgMTUuNjY2NjcsLTE1LjY2NjY3IDM0LjUsLTIzLjUgNTYuNSwtMjMuNSBoIDU2MCBjIDIyLDAgNDAuODMzMzMsNy44MzMzMyA1Ni41LDIzLjUgMTUuNjY2NjcsMTUuNjY2NjcgMjMuNSwzNC41IDIzLjUsNTYuNSB2IDU2MCBjIDAsMjIgLTcuODMzMzMsNDAuODMzMzMgLTIzLjUsNTYuNSAtMTUuNjY2NjcsMTUuNjY2NjcgLTM0LjUsMjMuNSAtNTYuNSwyMy41IHogbSAwLC04MCBIIDc2MCBWIC03NjAgSCAyMDAgWiBtIDAsLTU2MCB2IDU2MCB6IgogICAgIGlkPSJwYXRoMiIKICAgICBzb2RpcG9kaTpub2RldHlwZXM9InNzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3NjY2NjY2NjYyIKICAgICBzdHlsZT0iZmlsbDojN0Q1MjYwO2ZpbGwtb3BhY2l0eToxO3N0cm9rZTojN0Q1MjYwO3N0cm9rZS1vcGFjaXR5OjEiIC8+Cjwvc3ZnPgo=",
                description = "4",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 3,
                imageBase64 = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjxzdmcKICAgaGVpZ2h0PSIyNCIKICAgaWQ9InN2ZzQiCiAgIHZlcnNpb249IjEuMSIKICAgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiCiAgIHdpZHRoPSIyNCIKICAgaW5rc2NhcGU6dmVyc2lvbj0iMS4yLjIgKGIwYTg0ODY1NDEsIDIwMjItMTItMDEpIgogICBzb2RpcG9kaTpkb2NuYW1lPSJkNnMzLnN2ZyIKICAgeG1sbnM6aW5rc2NhcGU9Imh0dHA6Ly93d3cuaW5rc2NhcGUub3JnL25hbWVzcGFjZXMvaW5rc2NhcGUiCiAgIHhtbG5zOnNvZGlwb2RpPSJodHRwOi8vc29kaXBvZGkuc291cmNlZm9yZ2UubmV0L0RURC9zb2RpcG9kaS0wLmR0ZCIKICAgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIgogICB4bWxuczpzdmc9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8ZGVmcwogICAgIGlkPSJkZWZzOCIgLz4KICA8c29kaXBvZGk6bmFtZWR2aWV3CiAgICAgYm9yZGVyY29sb3I9IiM2NjY2NjYiCiAgICAgYm9yZGVyb3BhY2l0eT0iMS4wIgogICAgIGlkPSJuYW1lZHZpZXc2IgogICAgIHBhZ2Vjb2xvcj0iI2ZmZmZmZiIKICAgICBzaG93Z3JpZD0iZmFsc2UiCiAgICAgaW5rc2NhcGU6Y3VycmVudC1sYXllcj0ic3ZnNCIKICAgICBpbmtzY2FwZTpjeD0iMTIuMDIxNTA1IgogICAgIGlua3NjYXBlOmN5PSIxMiIKICAgICBpbmtzY2FwZTpwYWdlY2hlY2tlcmJvYXJkPSIwIgogICAgIGlua3NjYXBlOnBhZ2VvcGFjaXR5PSIwLjAiCiAgICAgaW5rc2NhcGU6cGFnZXNoYWRvdz0iMiIKICAgICBpbmtzY2FwZTp3aW5kb3ctaGVpZ2h0PSIxMDA4IgogICAgIGlua3NjYXBlOndpbmRvdy1tYXhpbWl6ZWQ9IjEiCiAgICAgaW5rc2NhcGU6d2luZG93LXdpZHRoPSIxOTIwIgogICAgIGlua3NjYXBlOndpbmRvdy14PSIwIgogICAgIGlua3NjYXBlOndpbmRvdy15PSIwIgogICAgIGlua3NjYXBlOnpvb209IjIzLjI1IgogICAgIGlua3NjYXBlOnNob3dwYWdlc2hhZG93PSIyIgogICAgIGlua3NjYXBlOmRlc2tjb2xvcj0iI2QxZDFkMSIgLz4KICA8cGF0aAogICAgIGQ9Im0gMzAwLC0yNDAgYyAxNi42NjY2NywwIDMwLjgzMzMzLC01LjgzMzMzIDQyLjUsLTE3LjUgMTEuNjY2NjcsLTExLjY2NjY3IDE3LjUsLTI1LjgzMzMzIDE3LjUsLTQyLjUgMCwtMTYuNjY2NjcgLTUuODMzMzMsLTMwLjgzMzMzIC0xNy41LC00Mi41IC0xMS42NjY2NywtMTEuNjY2NjcgLTI1LjgzMzMzLC0xNy41IC00Mi41LC0xNy41IC0xNi42NjY2NywwIC0zMC44MzMzMyw1LjgzMzMzIC00Mi41LDE3LjUgLTExLjY2NjY3LDExLjY2NjY3IC0xNy41LDI1LjgzMzMzIC0xNy41LDQyLjUgMCwxNi42NjY2NyA1LjgzMzMzLDMwLjgzMzMzIDE3LjUsNDIuNSAxMS42NjY2NywxMS42NjY2NyAyNS44MzMzMywxNy41IDQyLjUsMTcuNSB6IG0gMTgwLC0xODAgYyAxNi42NjY2NywwIDMwLjgzMzMzLC01LjgzMzMzIDQyLjUsLTE3LjUgMTEuNjY2NjcsLTExLjY2NjY3IDE3LjUsLTI1LjgzMzMzIDE3LjUsLTQyLjUgMCwtMTYuNjY2NjcgLTUuODMzMzMsLTMwLjgzMzMzIC0xNy41LC00Mi41IC0xMS42NjY2NywtMTEuNjY2NjcgLTI1LjgzMzMzLC0xNy41IC00Mi41LC0xNy41IC0xNi42NjY2NywwIC0zMC44MzMzMyw1LjgzMzMzIC00Mi41LDE3LjUgLTExLjY2NjY3LDExLjY2NjY3IC0xNy41LDI1LjgzMzMzIC0xNy41LDQyLjUgMCwxNi42NjY2NyA1LjgzMzMzLDMwLjgzMzMzIDE3LjUsNDIuNSAxMS42NjY2NywxMS42NjY2NyAyNS44MzMzMywxNy41IDQyLjUsMTcuNSB6IG0gMTgwLC0xODAgYyAxNi42NjY2NywwIDMwLjgzMzMzLC01LjgzMzMzIDQyLjUsLTE3LjUgMTEuNjY2NjcsLTExLjY2NjY3IDE3LjUsLTI1LjgzMzMzIDE3LjUsLTQyLjUgMCwtMTYuNjY2NjcgLTUuODMzMzMsLTMwLjgzMzMzIC0xNy41LC00Mi41IC0xMS42NjY2NywtMTEuNjY2NjcgLTI1LjgzMzMzLC0xNy41IC00Mi41LC0xNy41IC0xNi42NjY2NywwIC0zMC44MzMzMyw1LjgzMzMzIC00Mi41LDE3LjUgLTExLjY2NjY3LDExLjY2NjY3IC0xNy41LDI1LjgzMzMzIC0xNy41LDQyLjUgMCwxNi42NjY2NyA1LjgzMzMzLDMwLjgzMzMzIDE3LjUsNDIuNSAxMS42NjY2NywxMS42NjY2NyAyNS44MzMzMywxNy41IDQyLjUsMTcuNSB6IG0gLTQ2MCw0ODAgYyAtMjIsMCAtNDAuODMzMzMsLTcuODMzMzMgLTU2LjUsLTIzLjUgQyAxMjcuODMzMzMsLTE1OS4xNjY2NyAxMjAsLTE3OCAxMjAsLTIwMCB2IC01NjAgYyAwLC0yMiA3LjgzMzMzLC00MC44MzMzMyAyMy41LC01Ni41IDE1LjY2NjY3LC0xNS42NjY2NyAzNC41LC0yMy41IDU2LjUsLTIzLjUgaCA1NjAgYyAyMiwwIDQwLjgzMzMzLDcuODMzMzMgNTYuNSwyMy41IDE1LjY2NjY3LDE1LjY2NjY3IDIzLjUsMzQuNSAyMy41LDU2LjUgdiA1NjAgYyAwLDIyIC03LjgzMzMzLDQwLjgzMzMzIC0yMy41LDU2LjUgLTE1LjY2NjY3LDE1LjY2NjY3IC0zNC41LDIzLjUgLTU2LjUsMjMuNSB6IG0gMCwtODAgSCA3NjAgViAtNzYwIEggMjAwIFogbSAwLC01NjAgdiA1NjAgeiIKICAgICBpZD0icGF0aDIiCiAgICAgc29kaXBvZGk6bm9kZXR5cGVzPSJzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3NzY2NjY2NjY2MiCiAgICAgc3R5bGU9ImZpbGw6IzdENTI2MDtmaWxsLW9wYWNpdHk6MTtzdHJva2U6IzdENTI2MDtzdHJva2Utb3BhY2l0eToxIiAvPgo8L3N2Zz4K",
                description = "3",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 2,
                imageBase64 = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjxzdmcKICAgaGVpZ2h0PSIyNCIKICAgaWQ9InN2ZzQiCiAgIHZlcnNpb249IjEuMSIKICAgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiCiAgIHdpZHRoPSIyNCIKICAgaW5rc2NhcGU6dmVyc2lvbj0iMS4yLjIgKGIwYTg0ODY1NDEsIDIwMjItMTItMDEpIgogICBzb2RpcG9kaTpkb2NuYW1lPSJkNnMyLnN2ZyIKICAgeG1sbnM6aW5rc2NhcGU9Imh0dHA6Ly93d3cuaW5rc2NhcGUub3JnL25hbWVzcGFjZXMvaW5rc2NhcGUiCiAgIHhtbG5zOnNvZGlwb2RpPSJodHRwOi8vc29kaXBvZGkuc291cmNlZm9yZ2UubmV0L0RURC9zb2RpcG9kaS0wLmR0ZCIKICAgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIgogICB4bWxuczpzdmc9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8ZGVmcwogICAgIGlkPSJkZWZzOCIgLz4KICA8c29kaXBvZGk6bmFtZWR2aWV3CiAgICAgYm9yZGVyY29sb3I9IiM2NjY2NjYiCiAgICAgYm9yZGVyb3BhY2l0eT0iMS4wIgogICAgIGlkPSJuYW1lZHZpZXc2IgogICAgIHBhZ2Vjb2xvcj0iI2ZmZmZmZiIKICAgICBzaG93Z3JpZD0iZmFsc2UiCiAgICAgaW5rc2NhcGU6Y3VycmVudC1sYXllcj0ic3ZnNCIKICAgICBpbmtzY2FwZTpjeD0iMTIuMDIxNTA1IgogICAgIGlua3NjYXBlOmN5PSIxMiIKICAgICBpbmtzY2FwZTpwYWdlY2hlY2tlcmJvYXJkPSIwIgogICAgIGlua3NjYXBlOnBhZ2VvcGFjaXR5PSIwLjAiCiAgICAgaW5rc2NhcGU6cGFnZXNoYWRvdz0iMiIKICAgICBpbmtzY2FwZTp3aW5kb3ctaGVpZ2h0PSIxMDA4IgogICAgIGlua3NjYXBlOndpbmRvdy1tYXhpbWl6ZWQ9IjEiCiAgICAgaW5rc2NhcGU6d2luZG93LXdpZHRoPSIxOTIwIgogICAgIGlua3NjYXBlOndpbmRvdy14PSIwIgogICAgIGlua3NjYXBlOndpbmRvdy15PSIwIgogICAgIGlua3NjYXBlOnpvb209IjIzLjI1IgogICAgIGlua3NjYXBlOnNob3dwYWdlc2hhZG93PSIyIgogICAgIGlua3NjYXBlOmRlc2tjb2xvcj0iI2QxZDFkMSIgLz4KICA8cGF0aAogICAgIGQ9Im0gMzAwLC0yNDAgYyAxNi42NjY2NywwIDMwLjgzMzMzLC01LjgzMzMzIDQyLjUsLTE3LjUgMTEuNjY2NjcsLTExLjY2NjY3IDE3LjUsLTI1LjgzMzMzIDE3LjUsLTQyLjUgMCwtMTYuNjY2NjcgLTUuODMzMzMsLTMwLjgzMzMzIC0xNy41LC00Mi41IC0xMS42NjY2NywtMTEuNjY2NjcgLTI1LjgzMzMzLC0xNy41IC00Mi41LC0xNy41IC0xNi42NjY2NywwIC0zMC44MzMzMyw1LjgzMzMzIC00Mi41LDE3LjUgLTExLjY2NjY3LDExLjY2NjY3IC0xNy41LDI1LjgzMzMzIC0xNy41LDQyLjUgMCwxNi42NjY2NyA1LjgzMzMzLDMwLjgzMzMzIDE3LjUsNDIuNSAxMS42NjY2NywxMS42NjY2NyAyNS44MzMzMywxNy41IDQyLjUsMTcuNSB6IG0gMzYwLC0zNjAgYyAxNi42NjY2NywwIDMwLjgzMzMzLC01LjgzMzMzIDQyLjUsLTE3LjUgMTEuNjY2NjcsLTExLjY2NjY3IDE3LjUsLTI1LjgzMzMzIDE3LjUsLTQyLjUgMCwtMTYuNjY2NjcgLTUuODMzMzMsLTMwLjgzMzMzIC0xNy41LC00Mi41IC0xMS42NjY2NywtMTEuNjY2NjcgLTI1LjgzMzMzLC0xNy41IC00Mi41LC0xNy41IC0xNi42NjY2NywwIC0zMC44MzMzMyw1LjgzMzMzIC00Mi41LDE3LjUgLTExLjY2NjY3LDExLjY2NjY3IC0xNy41LDI1LjgzMzMzIC0xNy41LDQyLjUgMCwxNi42NjY2NyA1LjgzMzMzLDMwLjgzMzMzIDE3LjUsNDIuNSAxMS42NjY2NywxMS42NjY2NyAyNS44MzMzMywxNy41IDQyLjUsMTcuNSB6IG0gLTQ2MCw0ODAgYyAtMjIsMCAtNDAuODMzMzMsLTcuODMzMzMgLTU2LjUsLTIzLjUgQyAxMjcuODMzMzMsLTE1OS4xNjY2NyAxMjAsLTE3OCAxMjAsLTIwMCB2IC01NjAgYyAwLC0yMiA3LjgzMzMzLC00MC44MzMzMyAyMy41LC01Ni41IDE1LjY2NjY3LC0xNS42NjY2NyAzNC41LC0yMy41IDU2LjUsLTIzLjUgaCA1NjAgYyAyMiwwIDQwLjgzMzMzLDcuODMzMzMgNTYuNSwyMy41IDE1LjY2NjY3LDE1LjY2NjY3IDIzLjUsMzQuNSAyMy41LDU2LjUgdiA1NjAgYyAwLDIyIC03LjgzMzMzLDQwLjgzMzMzIC0yMy41LDU2LjUgLTE1LjY2NjY3LDE1LjY2NjY3IC0zNC41LDIzLjUgLTU2LjUsMjMuNSB6IG0gMCwtODAgSCA3NjAgViAtNzYwIEggMjAwIFogbSAwLC01NjAgdiA1NjAgeiIKICAgICBpZD0icGF0aDIiCiAgICAgc29kaXBvZGk6bm9kZXR5cGVzPSJzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3NzY2NjY2NjY2MiCiAgICAgc3R5bGU9ImZpbGw6IzdENTI2MDtmaWxsLW9wYWNpdHk6MTtzdHJva2U6IzdENTI2MDtzdHJva2Utb3BhY2l0eToxIiAvPgo8L3N2Zz4K",
                description = "2",
                descriptionColour = "FF6650a4",
            ),
            Side(
                number = 1,
                imageBase64 = "PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjxzdmcKICAgaGVpZ2h0PSIyNCIKICAgaWQ9InN2ZzQiCiAgIHZlcnNpb249IjEuMSIKICAgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiCiAgIHdpZHRoPSIyNCIKICAgaW5rc2NhcGU6dmVyc2lvbj0iMS4yLjIgKGIwYTg0ODY1NDEsIDIwMjItMTItMDEpIgogICBzb2RpcG9kaTpkb2NuYW1lPSJkNnMxLnN2ZyIKICAgeG1sbnM6aW5rc2NhcGU9Imh0dHA6Ly93d3cuaW5rc2NhcGUub3JnL25hbWVzcGFjZXMvaW5rc2NhcGUiCiAgIHhtbG5zOnNvZGlwb2RpPSJodHRwOi8vc29kaXBvZGkuc291cmNlZm9yZ2UubmV0L0RURC9zb2RpcG9kaS0wLmR0ZCIKICAgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIgogICB4bWxuczpzdmc9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8ZGVmcwogICAgIGlkPSJkZWZzOCIgLz4KICA8c29kaXBvZGk6bmFtZWR2aWV3CiAgICAgYm9yZGVyY29sb3I9IiM2NjY2NjYiCiAgICAgYm9yZGVyb3BhY2l0eT0iMS4wIgogICAgIGlkPSJuYW1lZHZpZXc2IgogICAgIHBhZ2Vjb2xvcj0iI2ZmZmZmZiIKICAgICBzaG93Z3JpZD0iZmFsc2UiCiAgICAgaW5rc2NhcGU6Y3VycmVudC1sYXllcj0ic3ZnNCIKICAgICBpbmtzY2FwZTpjeD0iMTIuMzE1MTMyIgogICAgIGlua3NjYXBlOmN5PSIxNC42ODM5MyIKICAgICBpbmtzY2FwZTpwYWdlY2hlY2tlcmJvYXJkPSIwIgogICAgIGlua3NjYXBlOnBhZ2VvcGFjaXR5PSIwLjAiCiAgICAgaW5rc2NhcGU6cGFnZXNoYWRvdz0iMiIKICAgICBpbmtzY2FwZTp3aW5kb3ctaGVpZ2h0PSIxMDA4IgogICAgIGlua3NjYXBlOndpbmRvdy1tYXhpbWl6ZWQ9IjEiCiAgICAgaW5rc2NhcGU6d2luZG93LXdpZHRoPSIxOTIwIgogICAgIGlua3NjYXBlOndpbmRvdy14PSIwIgogICAgIGlua3NjYXBlOndpbmRvdy15PSIwIgogICAgIGlua3NjYXBlOnpvb209IjM4LjIwNTAzMSIKICAgICBpbmtzY2FwZTpzaG93cGFnZXNoYWRvdz0iMiIKICAgICBpbmtzY2FwZTpkZXNrY29sb3I9IiNkMWQxZDEiIC8+CiAgPHBhdGgKICAgICBkPSJtIDQ4MCwtNDIwIGMgMTYuNjY2NjcsMCAzMC44MzMzMywtNS44MzMzMyA0Mi41LC0xNy41IDExLjY2NjY3LC0xMS42NjY2NyAxNy41LC0yNS44MzMzMyAxNy41LC00Mi41IDAsLTE2LjY2NjY3IC01LjgzMzMzLC0zMC44MzMzMyAtMTcuNSwtNDIuNSAtMTEuNjY2NjcsLTExLjY2NjY3IC0yNS44MzMzMywtMTcuNSAtNDIuNSwtMTcuNSAtMTYuNjY2NjcsMCAtMzAuODMzMzMsNS44MzMzMyAtNDIuNSwxNy41IC0xMS42NjY2NywxMS42NjY2NyAtMTcuNSwyNS44MzMzMyAtMTcuNSw0Mi41IDAsMTYuNjY2NjcgNS44MzMzMywzMC44MzMzMyAxNy41LDQyLjUgMTEuNjY2NjcsMTEuNjY2NjcgMjUuODMzMzMsMTcuNSA0Mi41LDE3LjUgeiBtIC0yODAsMzAwIGMgLTIyLDAgLTQwLjgzMzMzLC03LjgzMzMzIC01Ni41LC0yMy41IEMgMTI3LjgzMzMzLC0xNTkuMTY2NjcgMTIwLC0xNzggMTIwLC0yMDAgdiAtNTYwIGMgMCwtMjIgNy44MzMzMywtNDAuODMzMzMgMjMuNSwtNTYuNSAxNS42NjY2NywtMTUuNjY2NjcgMzQuNSwtMjMuNSA1Ni41LC0yMy41IGggNTYwIGMgMjIsMCA0MC44MzMzMyw3LjgzMzMzIDU2LjUsMjMuNSAxNS42NjY2NywxNS42NjY2NyAyMy41LDM0LjUgMjMuNSw1Ni41IHYgNTYwIGMgMCwyMiAtNy44MzMzMyw0MC44MzMzMyAtMjMuNSw1Ni41IC0xNS42NjY2NywxNS42NjY2NyAtMzQuNSwyMy41IC01Ni41LDIzLjUgeiBtIDAsLTgwIEggNzYwIFYgLTc2MCBIIDIwMCBaIG0gMCwtNTYwIHYgNTYwIHoiCiAgICAgaWQ9InBhdGgyIgogICAgIHNvZGlwb2RpOm5vZGV0eXBlcz0ic3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc3Nzc2NjY2NjY2NjIgogICAgIHN0eWxlPSJmaWxsOiM3RDUyNjA7ZmlsbC1vcGFjaXR5OjE7c3Ryb2tlOiM3RDUyNjA7c3Ryb2tlLW9wYWNpdHk6MSIgLz4KPC9zdmc+Cg==",
                description = "1",
                descriptionColour = "FF6650a4",
            ),
        ),
        title = "d6",
        selected = true,
        multiplierValue = 3,
    )

    val diceStory = Dice(
        sides = listOf(
            Side(
                number = 6,
                description = "Knight",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_knight,
            ),
            Side(
                number = 5,
                description = "Lion",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_lion,
            ),
            Side(
                number = 4,
                description = "Pirate",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_pirate,
            ),
            Side(
                number = 3,
                description = "Crocodile",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_crocodile,
            ),
            Side(
                number = 2,
                description = "Queen",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_queen,
            ),
            Side(
                number = 1,
                description = "Spaceship",
                descriptionColour = "FF6650a4",
                imageDrawableId = R.drawable.story_spaceship,
            ),
        ),
        title = "Story",
        multiplierValue = 4,
    )

    val allDice = mutableListOf(
        d6,
        diceStory
    )
}
