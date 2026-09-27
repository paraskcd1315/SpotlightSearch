#!/usr/bin/env bash
set -euo pipefail

repo="$(cd "$(dirname "$0")/.." && pwd)"
store_file="${SPOT_KEYSTORE:-$HOME/Documents/keystores-android/keystore-2025-05-24-04h45m}"
key_alias="${SPOT_KEY_ALIAS:-keystore-2025-05-25-04h45m}"
service_prefix="IntelliJ Platform APK Signing Keystore Step — "
target="$repo/keystore.properties"

escape() { printf '%s' "${1//\\/\\\\}"; }

read_keychain() {
    security find-generic-password -s "$1" -w 2>/dev/null || {
        echo "Not in the login Keychain: $1" >&2
        exit 1
    }
}

[[ -f "$store_file" ]] || { echo "Keystore not found: $store_file" >&2; exit 1; }

store_password="$(read_keychain "${service_prefix}KEY_STORE_PASSWORD__${store_file}")"
key_password="$(read_keychain "${service_prefix}KEY_PASSWORD__${store_file}__${key_alias}")"

umask 077
{
    printf 'storeFile=%s\n' "$(escape "$store_file")"
    printf 'storePassword=%s\n' "$(escape "$store_password")"
    printf 'keyAlias=%s\n' "$(escape "$key_alias")"
    printf 'keyPassword=%s\n' "$(escape "$key_password")"
} > "$target"
chmod 600 "$target"
unset store_password key_password

echo "Wrote $target from the login Keychain."

if [[ "${1:-}" != "--no-check" ]]; then
    echo "Checking the release signing config..."
    report="$(cd "$repo" && ./gradlew :app:signingReport --console=plain 2>&1)" || true
    printf '%s\n' "$report" | grep -A6 '^Variant: release$' || printf '%s\n' "$report" | tail -20
fi
