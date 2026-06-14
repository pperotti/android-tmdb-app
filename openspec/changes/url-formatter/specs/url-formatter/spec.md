# url-formatter spec

Summary

A presentation-layer formatter that converts raw URL strings into display-ready, tappable links for the Movie Details screen and exposes a safe onClick handler that opens the device's default browser.

Requirements

- The formatter accepts a nullable input: rawUrl: String?
- If rawUrl is null, the formatter must return immediately and perform no side-effects.
- The formatter must validate that the input is a well-formed URL according to standard URL syntax (scheme, host, optional path/query/fragment).
- Only allow URLs using the http or https schemes. Any other scheme (e.g., ftp:, file:, data:, intent:) must be rejected.
- Leading/trailing whitespace must be trimmed before validation.
- URLs missing a host or otherwise failing parsing should be treated as invalid and not presented as tappable links.

Output contract

- On valid input, the formatter returns an object/value that includes:
  - displayText: String — the text to render in the UI (preferably the host or a human-friendly representation when appropriate).
  - launchIntent(uri: Uri) / onClick handler — a callback or lambda that, when invoked, opens the default browser with the validated URL (ACTION_VIEW semantic).
  - rawUrl: String — the validated, normalized (trimmed) URL string.
- On invalid input (including non-http(s) schemes), the formatter returns null or an explicit result indicating "no link" so the UI can render plain text or hide the element.

Behavior and Safety

- The formatter must not perform navigation itself; it only exposes a validated onClick handler. The UI layer invokes the handler when the user taps the link.
- The onClick handler must launch an external browser using platform-standard mechanisms (e.g., Android Intent with ACTION_VIEW) and include minimal safety checks (e.g., confirm the URL is still valid). Do not use in-app WebView by default.
- The formatter should avoid exposing malformed or potentially harmful URIs. Reject data:, javascript:, intent: and similar schemes.

Accessibility

- Links must include contentDescription/accessibility label describing the target (e.g., "Open official website") when context allows. If the origin is unknown, use a generic label like "Open link".
- Ensure focus and touch targets respect platform accessibility guidelines.

Edge cases

- URL contains only whitespace -> treated as null/invalid.
- URLs lacking scheme (e.g., "www.example.com") -> treat as invalid unless upstream guarantees scheme; prefer explicit rejection to avoid accidental implicit intent behavior.
- Internationalized domain names should be validated via standard URL parsing libraries.

Testing / Acceptance criteria

Unit tests
- Null input returns immediately (no link object, no side-effects).
- Valid http and https URLs produce a link object with expected displayText and a non-null onClick handler.
- URLs with disallowed schemes (ftp:, file:, data:, javascript:) return invalid/no-link.
- URLs with missing host or malformed structure return invalid/no-link.
- Leading/trailing whitespace is trimmed before validation.

Integration tests (UI/instrumented)
- Movie Details screen renders a tappable link for a valid external URL and tapping it launches the default browser with the same URL.
- Invalid or null URLs render no tappable link.

Examples

- Input: null -> Result: null (no link)
- Input: " https://www.example.com/movie/123 " -> Result: valid link, displayText: "www.example.com", rawUrl: "https://www.example.com/movie/123"
- Input: "ftp://files.example.com" -> Result: invalid (no link)
- Input: "javascript:alert(1)" -> Result: invalid (no link)

Implementation notes (non-normative)

- Use platform URL parsing utilities (e.g., java.net.URI/URL or Android Uri.parse) for robust parsing rather than regex.
- Consider returning a small data class, e.g.:
  data class FormattedUrl(val displayText: String, val rawUrl: String, val onClick: () -> Unit)
- Keep the formatter in the presentation layer (presentation/common or presentation/ui). Unit-test without Android framework when possible.


