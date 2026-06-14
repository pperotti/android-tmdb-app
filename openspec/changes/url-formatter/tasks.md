## 1. Setup

- [x] 1.1 Create UrlFormatter.kt under presentation/common and add package declaration
- [x] 1.2 Add unit-test file UrlFormatterTest.kt under app/src/test for formatter logic

## 2. Core Implementation

- [x] 2.1 Implement FormattedUrl data class and UrlFormatter.parse(rawUrl: String?): FormattedUrl? with trimming and validation (http/https only)
- [x] 2.2 Implement onClick lambda to accept Context and launch ACTION_VIEW intent with safe checks
- [x] 2.3 Add logging/metrics hooks where failures occur (invalid URL, no activity to handle intent)

## 3. Integration

- [x] 3.1 Update Movie Details screen to use UrlFormatter for external link fields and render clickable UI
- [x] 3.2 Wire accessibility labels for the rendered link (contentDescription) based on context
- [x] 3.3 Add UI adapter/code that converts FormattedUrl.onClick into Compose clickable modifier or View.OnClickListener

## 4. Tests

- [x] 4.1 Unit tests: null input, valid http/https inputs, disallowed schemes, whitespace trimming
- [x] 4.2 Instrumented UI test: verify tapping link fires ACTION_VIEW intent with expected URL (Espresso-Intents)
- [x] 4.3 Add tests for fallback behavior when no activity can handle intent

## 5. Documentation & PR

- [x] 5.1 Add a short note in CHANGELOG or docs describing the new capability and where to use it
- [ ] 5.2 Create a PR with description referencing openspec/changes/url-formatter and include screenshots or intent test logs

