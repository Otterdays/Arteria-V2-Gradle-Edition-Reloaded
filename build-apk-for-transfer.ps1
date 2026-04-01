# Build an APK in this project and copy it to dist/ with a timestamp for Google Drive (or any transfer).
# Usage:
#   .\build-apk-for-transfer.ps1
#   .\build-apk-for-transfer.ps1 -Variant release
#   .\build-apk-for-transfer.ps1 -Clean
# Release builds require signing configured in app/build.gradle.kts; use debug if unsure.

param(
    [ValidateSet("debug", "release")]
    [string] $Variant = "debug",
    [switch] $Clean
)

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
Set-Location $root

# PowerShell inherits Cursor's JAVA_HOME (Red Hat JRE, no jlink) — AGP androidJdkImage fails.
# Resolve a full JDK before gradlew.bat (same rules as gradlew.bat preamble).
function Resolve-ArteriaJdkHome {
    param([string] $CurrentHome)
    $isPoisoned = {
        param([string] $p)
        if ([string]::IsNullOrWhiteSpace($p)) { return $false }
        $u = $p.ToLowerInvariant()
        return ($u.Contains('.cursor') -or $u.Contains('redhat.java'))
    }
    if ($CurrentHome -and -not (& $isPoisoned $CurrentHome) -and
        (Test-Path (Join-Path $CurrentHome 'bin/jlink.exe'))) {
        return $CurrentHome
    }
    foreach ($name in @('jdk-21.0.10', 'jdk-26', 'jdk-21')) {
        $p = Join-Path $env:ProgramFiles "Java\$name"
        if (Test-Path (Join-Path $p 'bin/jlink.exe')) { return $p }
    }
    $adoptium = Join-Path $env:ProgramFiles 'Eclipse Adoptium'
    if (Test-Path $adoptium) {
        foreach ($dir in (Get-ChildItem $adoptium -Directory -ErrorAction SilentlyContinue | Where-Object { $_.Name -like 'jdk-*' })) {
            $jp = $dir.FullName
            if (Test-Path (Join-Path $jp 'bin/jlink.exe')) { return $jp }
        }
    }
    $ms = Join-Path $env:ProgramFiles 'Microsoft'
    if (Test-Path $ms) {
        foreach ($dir in (Get-ChildItem $ms -Directory -Filter 'jdk-*' -ErrorAction SilentlyContinue)) {
            $jp = $dir.FullName
            if (Test-Path (Join-Path $jp 'bin/jlink.exe')) { return $jp }
        }
    }
    return $null
}

$resolved = Resolve-ArteriaJdkHome -CurrentHome $env:JAVA_HOME
if (-not $resolved) {
    Write-Error @"
No full JDK with bin\jlink.exe found. Android Gradle Plugin needs jlink (not in Cursor's JRE).
Install Temurin JDK 21+ to Program Files\Java or Eclipse Adoptium, or set JAVA_HOME to that JDK.
"@
}
$env:JAVA_HOME = $resolved
$env:Path = "$(Join-Path $resolved 'bin');$env:Path"
Write-Host "JAVA_HOME -> $resolved" -ForegroundColor DarkGray

$gradlew = Join-Path $root "gradlew.bat"
if (-not (Test-Path $gradlew)) {
    Write-Error "gradlew.bat not found in $root"
}

$task = if ($Variant -eq "release") { ":app:assembleRelease" } else { ":app:assembleDebug" }
$gradleArgs = @($task)
if ($Clean) {
    $gradleArgs = @("clean") + $gradleArgs
}

Write-Host "Gradle: gradlew.bat $($gradleArgs -join ' ')" -ForegroundColor Cyan
& $gradlew --stop 2>$null
& $gradlew @gradleArgs
if ($LASTEXITCODE -ne 0) {
    Write-Error "Gradle failed with exit code $LASTEXITCODE"
}

$apkRoot = Join-Path $root "app/build/outputs/apk"
if (-not (Test-Path $apkRoot)) {
    Write-Error "No APK output at $apkRoot - check that the :app module built successfully."
}

$built = Get-ChildItem -Path $apkRoot -Recurse -Filter "*.apk" -File |
    Sort-Object LastWriteTime -Descending |
    Select-Object -First 1

if (-not $built) {
    Write-Error "No .apk found under $apkRoot after build."
}

$dist = Join-Path $root "dist"
New-Item -ItemType Directory -Path $dist -Force | Out-Null
$stamp = Get-Date -Format "yyyyMMdd-HHmmss"
# Product name: Arteria V2 Gradle Edition Reloaded (kebab-case for safe filenames).
$destName = "Arteria-V2-Gradle-Edition-Reloaded-$Variant-$stamp.apk"
$dest = Join-Path $dist $destName

Copy-Item -LiteralPath $built.FullName -Destination $dest -Force
Write-Host ""
Write-Host "Copied transfer APK:" -ForegroundColor Green
Write-Host $dest
Write-Host ""
Write-Host 'Upload that file to Google Drive (or share however you like).' -ForegroundColor Gray
