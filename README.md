# 🐦‍⬛ Evee's Patches

## ❓ About

Patches for apps/ideas I use, made for personal use that I figured I could share because maybe someone else would like them too. Made with heavy assistance from AI tools (mostly Antigravity/Gemini). I am not opposed to hearing about any ideas for new patches, or bug reports. However, I am by no means a professional developer, so please do not expect miracles. I literally only work on this in my free time, and when I have nothing better to do with it.

<!-- TODO: Update this about section with a brief introduction/summary about this repo and what it offers. -->

### How to use these patches

Click here to add these patches to Morphe: https://morphe.software/add-source?github=Eveelogy/morphe-patches

Or you can go to the Patches sources section in the Morphe app and add the GitHub repository URL manually: https://github.com/Eveelogy/morphe-patches

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
> **[v1.0.0-dev.10](https://github.com/Eveelogy/morphe-patches/releases/tag/v1.0.0-dev.10)**&nbsp;&nbsp;•&nbsp;&nbsp;`dev`&nbsp;&nbsp;•&nbsp;&nbsp;4 patches total
<details open>
<summary>📦 Gboard&nbsp;&nbsp;•&nbsp;&nbsp;4 patches</summary>
<br>

**🎯 Supported versions:**

| 18.0.3 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [Disable GIF SafeSearch](#disable-gif-safesearch) | Disables content filtering on Tenor GIF search. |  |
| [Morphe Settings](#morphe-settings) | Adds a dedicated Morphe Settings sub-menu in Gboard settings. |  |
| [Rename Gboard](#rename-gboard) | Changes the display name of Gboard in the keyboard switcher and settings. | • Keyboard name |
| [Replace emoticons with meme search](#replace-emoticons-with-meme-search) | Replaces the emoticon keyboard tab with a meme and static image search panel. |  |

</details>

<!-- PATCHES_END -->

### 🛠️ Building locally

- Run `./gradlew buildAndroid`
- The built patches .mpp file is found in `patches/build/libs/patches-*.mpp`
- Patch the mpp file using [Morphe-Desktop](https://github.com/MorpheApp/morphe-desktop)
  like any other patch bundle.

See the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation) for more information.

## 📜 License

Evee's Patches are licensed under the [GNU General Public License v3.0](LICENSE)
