# Change Log
All notable changes to this project will be documented in this file.

## [Unreleased]

## [v1.2]
### Added
* Add Automatic-Module-Name to "org.omegat.mnemonics"
* Add `AbstractMnemonicsAction` to help create Action objects with mnemonics.
* Publish `Mnemonics.getLatinKeycode` function

### Removed
* Russian translation resources

### Changed
* Bump Gradle@8.5
* Bump gradle-nexus-publish-plugin@1.3

## [v1.1]
### Added
* Introduce `Mnemonics.removeMnemonics`
* Test cases

### Changed
* Licensed on GNU General Public License Version 3.0 or later.
* Bump versions
  - Java 8
  - Gradle@7.5.1
  - JUnit@4.13.2
* Setup CI checks
  - Dependabot
  - automatically run gradle check
  - CodeQL analysis
  - Spotbugs analysis
  - on macOS and Linux
* Start recording change log
* Gradle: nexus-publish plugin
* Gradle: signing
* Gradle: set duplicates strategy to be INCLUDE

### Fixed
* Javadoc errors

## v1.0
* First release

[Unreleased]: https://github.com/eb4j/dsl4j/compare/v1.2...HEAD
[v1.2]: https://github.com/omegat-org/lib-mnemonics/compare/v1.1...v1.2
[v1.1]: https://github.com/omegat-org/lib-mnemonics/compare/v1.0...v1.1

