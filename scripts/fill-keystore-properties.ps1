param(
    [string]$StoreFile = "$env:USERPROFILE\Documents\keystores\keystore-2025-05-24-04h45m",
    [string]$KeyAlias = "keystore-2025-05-25-04h45m"
)

$ErrorActionPreference = "Stop"
$repo = Split-Path -Parent $PSScriptRoot
$target = Join-Path $repo "keystore.properties"

if (-not (Test-Path $StoreFile)) { throw "Keystore not found: $StoreFile" }

function Read-Secret([string]$prompt) {
    $secure = Read-Host -Prompt $prompt -AsSecureString
    $pointer = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secure)
    try { [Runtime.InteropServices.Marshal]::PtrToStringBSTR($pointer) }
    finally { [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($pointer) }
}

function Escape([string]$value) { $value.Replace("\", "\\") }

$storePassword = Read-Secret "Keystore password"
$keyPassword = Read-Secret "Key password (Enter to reuse the keystore password)"
if ([string]::IsNullOrEmpty($keyPassword)) { $keyPassword = $storePassword }

$lines = @(
    "storeFile=$(Escape $StoreFile.Replace('\', '/'))",
    "storePassword=$(Escape $storePassword)",
    "keyAlias=$(Escape $KeyAlias)",
    "keyPassword=$(Escape $keyPassword)"
)
[IO.File]::WriteAllLines($target, $lines)
Remove-Variable storePassword, keyPassword

Write-Host "Wrote $target. Checking the release signing config..."
$env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
& (Join-Path $repo "gradlew.bat") -p $repo :app:signingReport --console=plain | Select-String -Pattern "^Variant: release$" -Context 0, 6
