## Context

The `url-formatter` feature provides a presentation-layer utility to convert raw URL strings into display-ready, tappable links for the Movie Details screen. The specs require null-safe behavior, strict validation (http/https only), trimming whitespace, and returning no link for invalid inputs. This design focuses on a small, testable implementation that avoids in-app webviews and uses the platform ACTION_VIEW intent to open external browsers.

## Goals / Non-Goals

**Goals:**
- Provide a reusable, framework-agnostic formatter in the presentation layer.
- Expose a small immutable result object with displayText, rawUrl, and an onClick lambda that launches an external browser when invoked.
- Be unit-testable without Android instrumentation where possible.
- Ensure accessibility labels are available for links rendered in UI.

**Non-Goals:**
- Adding in-app WebView navigation.
- Changing domain models or repository APIs.
- Automatically shortening or rewriting URLs beyond basic normalization (trim).

## Decisions

1. Location and API
- Place implementation under: app/src/main/java/<package>/presentation/common/UrlFormatter.kt (or presentation/ui/common).
- Return data class:
  data class FormattedUrl(val displayText: String, val rawUrl: String, val onClick: (Context) -> Unit)
  Rationale: keeping Context out of formatter allows pure validation in unit tests; onClick accepts Context to perform the Intent launch in UI layer or wherever appropriate.

2. Validation & Parsing
- Use java.net.URI for parsing and validation or Android Uri.parse for normalization, but rely on java.net.URI for scheme/host checks in unit tests.
- Steps: null-check -> trim -> parse -> check scheme in {"http","https"} -> ensure host present -> return normalized rawUrl.

3. onClick behavior
- onClick will be a lambda that takes an Android Context and invokes:
  val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rawUrl)).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }
  context.startActivity(intent)
- Verify there is an activity to handle the intent before launching (packageManager.resolveActivity) and handle ActivityNotFoundException gracefully (log + optional user-facing toast handled by UI layer).

4. Display text
- Prefer showing the host (uri.host) + optional path summary if needed; fall back to rawUrl if host absent (should be invalid per spec).
- Keep displayText short (host) to avoid layout issues.

5. Dependency injection & testability
- Implement formatter as a simple object or class with pure methods; injection not required. Provide a thin adapter in UI code to convert FormattedUrl.onClick into a Compose clickable modifier or View.OnClickListener that calls it with Context.

## Risks / Trade-offs

- Risk: Some valid-looking inputs without scheme ("www.example.com") will be rejected; mitigation: document the requirement and ensure upstream includes schemes when possible.
- Risk: Intents may fail on devices without browsers; mitigation: check resolveActivity and surface graceful fallback via UI.
- Trade-off: Keeping Context out of formatter improves testability but requires the UI to supply Context when invoking onClick.

## Migration Plan

- Add FormattedUrl and UrlFormatter implementation and unit tests.
- Update Movie Details screen to use UrlFormatter for external link fields and wire link clicks to call onClick(context).
- Add instrumented test verifying an intent is fired when tapping link (use Intents/Mockito or Espresso-Intents).

## Open Questions

- Should displayText include path fragments for long hosts? (Prefer host-only for now)
- Should we add telemetry when a user opens external links? (out of scope)
