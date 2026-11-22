param([switch]$Test)
$ErrorActionPreference = 'Stop'
$projectDir = $PSScriptRoot
$buildDir = Join-Path $projectDir 'build'
$classesDir = Join-Path $buildDir 'classes'
$library = Join-Path $projectDir 'lib/flatlaf-3.8.jar'
$javac = if ($env:JAVA_HOME -and (Test-Path (Join-Path $env:JAVA_HOME 'bin/javac.exe'))) { Join-Path $env:JAVA_HOME 'bin/javac.exe' } else { 'javac' }
$java = if ($env:JAVA_HOME -and (Test-Path (Join-Path $env:JAVA_HOME 'bin/java.exe'))) { Join-Path $env:JAVA_HOME 'bin/java.exe' } else { 'java' }
$jar = if ($env:JAVA_HOME -and (Test-Path (Join-Path $env:JAVA_HOME 'bin/jar.exe'))) { Join-Path $env:JAVA_HOME 'bin/jar.exe' } else { 'jar' }
if (-not (Test-Path -LiteralPath $library)) { throw 'Missing lib/flatlaf-3.8.jar' }
New-Item -ItemType Directory -Force -Path $classesDir | Out-Null
$sources = @(Get-ChildItem -LiteralPath (Join-Path $projectDir 'src') -Recurse -Filter '*.java' | ForEach-Object { $_.FullName })
& $javac --release 15 -encoding UTF-8 -cp $library -d $classesDir @sources
if ($LASTEXITCODE -ne 0) { throw 'Java compilation failed. Use JDK 15 or newer.' }
Copy-Item -LiteralPath (Join-Path $projectDir 'src/images') -Destination $classesDir -Recurse -Force
$runtimeLib = Join-Path $buildDir 'lib'
New-Item -ItemType Directory -Force -Path $runtimeLib | Out-Null
Copy-Item -LiteralPath $library -Destination $runtimeLib -Force
Copy-Item -LiteralPath (Join-Path $projectDir 'lib/flatlaf-LICENSE.txt') -Destination $runtimeLib -Force
$manifest = "Manifest-Version: 1.0`nMain-Class: ui.Login`nClass-Path: lib/flatlaf-3.8.jar`n`n"
[IO.File]::WriteAllText((Join-Path $buildDir 'MANIFEST.MF'), $manifest, [Text.Encoding]::ASCII)
& $jar --create --file (Join-Path $buildDir 'ceramicos-app.jar') --manifest (Join-Path $buildDir 'MANIFEST.MF') -C $classesDir .
if ($LASTEXITCODE -ne 0) { throw 'JAR packaging failed.' }
[IO.File]::WriteAllText((Join-Path $buildDir 'Iniciar.bat'), "@echo off`r`ncd /d `"%~dp0`"`r`njava -jar ceramicos-app.jar`r`npause`r`n", [Text.Encoding]::ASCII)
Compress-Archive -Path (Join-Path $buildDir 'ceramicos-app.jar'),$runtimeLib,(Join-Path $buildDir 'Iniciar.bat') -DestinationPath (Join-Path $buildDir 'ceramicos-portable.zip') -Force
if ($Test) {
    $testDir = Join-Path $buildDir 'test-classes'
    New-Item -ItemType Directory -Force -Path $testDir | Out-Null
    $testSources = @(Get-ChildItem -LiteralPath (Join-Path $projectDir 'tests') -Recurse -Filter '*.java' | ForEach-Object { $_.FullName })
    & $javac --release 15 -encoding UTF-8 -cp "$classesDir;$library" -d $testDir @testSources
    if ($LASTEXITCODE -ne 0) { throw 'Test compilation failed.' }
    $capturesDir = Join-Path $buildDir 'capturas'
    $applicationJar = Join-Path $buildDir 'ceramicos-app.jar'
    & $java '-Dsun.java2d.uiScale=1' "-Dceramicos.captures=$capturesDir" -cp "$testDir;$applicationJar" checks.CeramicosCheck
    if ($LASTEXITCODE -ne 0) { throw 'Application checks failed.' }
}
Write-Output ('Ready: ' + (Join-Path $buildDir 'ceramicos-app.jar'))
