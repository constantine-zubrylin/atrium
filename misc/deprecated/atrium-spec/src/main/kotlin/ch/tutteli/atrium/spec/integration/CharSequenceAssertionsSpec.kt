// TODO remove file with 1.0.0
@file:Suppress("DEPRECATION", "TYPEALIAS_EXPANSION_DEPRECATION")

package ch.tutteli.atrium.spec.integration

import ch.tutteli.atrium.api.cc.en_GB.contains
import ch.tutteli.atrium.api.cc.en_GB.containsNot
import ch.tutteli.atrium.api.cc.en_GB.endsWith
import ch.tutteli.atrium.api.cc.en_GB.message
import ch.tutteli.atrium.api.cc.en_GB.messageContains
import ch.tutteli.atrium.api.cc.en_GB.toThrow
import ch.tutteli.atrium.creating.Assert
import ch.tutteli.atrium.reporting.translating.StringBasedTranslatable
import ch.tutteli.atrium.reporting.translating.Translatable
import ch.tutteli.atrium.reporting.translating.Untranslatable
import ch.tutteli.atrium.spec.AssertionVerbFactory
import ch.tutteli.atrium.spec.describeFun
import ch.tutteli.atrium.translations.DescriptionBasic
import ch.tutteli.atrium.translations.DescriptionCharSequenceAssertion
import ch.tutteli.atrium.translations.DescriptionCharSequenceAssertion.ENDS_NOT_WITH
import ch.tutteli.atrium.translations.DescriptionCharSequenceAssertion.ENDS_WITH
import ch.tutteli.atrium.translations.DescriptionCharSequenceAssertion.STARTS_NOT_WITH
import ch.tutteli.atrium.translations.DescriptionCharSequenceAssertion.STARTS_WITH
import org.jetbrains.spek.api.dsl.SpecBody
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.include

@Deprecated("Switch from Assert to Expect and use Spec from atrium-specs-common; will be removed with 1.0.0")
abstract class CharSequenceAssertionsSpec(
    verbs: AssertionVerbFactory,
    containsDefaultTranslationOfPair: Pair<String, Assert<CharSequence>.(Translatable, Array<out Translatable>) -> Assert<CharSequence>>,
    containsNotDefaultTranslationOfPair: Pair<String, Assert<CharSequence>.(Translatable, Array<out Translatable>) -> Assert<CharSequence>>,
    isEmptyPair: Pair<String, Assert<CharSequence>.() -> Assert<CharSequence>>,
    isNotEmptyPair: Pair<String, Assert<CharSequence>.() -> Assert<CharSequence>>,
    isNotBlankPair: Pair<String, Assert<CharSequence>.() -> Assert<CharSequence>>,
    startsWithPair: Pair<String, Assert<CharSequence>.(CharSequence) -> Assert<CharSequence>>,
    startsNotWithPair: Pair<String, Assert<CharSequence>.(CharSequence) -> Assert<CharSequence>>,
    endsWithPair: Pair<String, Assert<CharSequence>.(CharSequence) -> Assert<CharSequence>>,
    endsNotWithPair: Pair<String, Assert<CharSequence>.(CharSequence) -> Assert<CharSequence>>,
    rootBulletPoint: String,
    listBulletPoint: String,
    describePrefix: String = "[Atrium] "
) : CharSequenceContainsSpecBase({

    include(@Suppress("DEPRECATION") object : SubjectLessAssertionSpec<CharSequence>(describePrefix,
        containsDefaultTranslationOfPair.first to mapToCreateAssertion { containsDefaultTranslationOfPair.second(this, Untranslatable("a"), arrayOf()) },
        containsNotDefaultTranslationOfPair.first to mapToCreateAssertion { containsNotDefaultTranslationOfPair.second(this, Untranslatable("a"), arrayOf()) },
        isEmptyPair.first to mapToCreateAssertion { isEmptyPair.second(this) },
        isNotEmptyPair.first to mapToCreateAssertion { isNotEmptyPair.second(this) },
        isNotBlankPair.first to mapToCreateAssertion { isNotBlankPair.second(this) },
        startsWithPair.first to mapToCreateAssertion { startsWithPair.second(this, "") },
        startsNotWithPair.first to mapToCreateAssertion { startsNotWithPair.second(this, "") },
        endsWithPair.first to mapToCreateAssertion { endsWithPair.second(this, "") },
        endsNotWithPair.first to mapToCreateAssertion { endsNotWithPair.second(this, "") }
    ) {})

    include(@Suppress("DEPRECATION") object : CheckingAssertionSpec<String>(verbs, describePrefix,
        checkingTriple(containsDefaultTranslationOfPair.first, { containsDefaultTranslationOfPair.second(this, Untranslatable("a"), arrayOf()) }, "a", "b"),
        checkingTriple(containsNotDefaultTranslationOfPair.first, { containsNotDefaultTranslationOfPair.second(this, Untranslatable("a"), arrayOf()) }, "b", "a"),
        checkingTriple(isEmptyPair.first, { isEmptyPair.second(this) }, "", "not empty"),
        checkingTriple(isNotEmptyPair.first, { isNotEmptyPair.second(this) }, "not empty", ""),
        checkingTriple(isNotBlankPair.first, { isNotBlankPair.second(this) }, "not blank", ""),
        checkingTriple(startsWithPair.first, { startsWithPair.second(this, "a") }, "abc", "xyz"),
        checkingTriple(startsNotWithPair.first, { startsNotWithPair.second(this, "a") }, "xyz", "abc"),
        checkingTriple(endsWithPair.first, { endsWithPair.second(this, "c") }, "abc", "xyz"),
        checkingTriple(endsNotWithPair.first, { endsNotWithPair.second(this, "c") }, "xyz", "abc")
    ) {})

    fun describeFun(vararg funName: String, body: SpecBody.() -> Unit)
        = describeFun(describePrefix, funName, body = body)

    val assert: (CharSequence) -> Assert<CharSequence> = verbs::checkImmediately
    val expect = verbs::checkException
    val fluent = assert(text)

    val (containsDefaultTranslationOf, containsDefaultTranslationOfFunArr) = containsDefaultTranslationOfPair
    val (containsNotDefaultTranslationOf, containsNotDefaultTranslationOfFunArr) = containsNotDefaultTranslationOfPair
    val (isEmpty, isEmptyFun) = isEmptyPair
    val (isNotEmpty, isNotEmptyFun) = isNotEmptyPair
    val (isNotBlank, isNotBlankFun) = isNotBlankPair
    val (startsWith, startsWithFun) = startsWithPair
    val (startsNotWith, startsNotWithFun) = startsNotWithPair
    val (endsWith, endsWithFun) = endsWithPair
    val (endsNotWith, endsNotWithFun) = endsNotWithPair

    fun Assert<CharSequence>.containsDefaultTranslationOfFun(t: Translatable, vararg tX: Translatable)
        = containsDefaultTranslationOfFunArr(t, tX)

    fun Assert<CharSequence>.containsNotDefaultTranslationOfFun(t: Translatable, vararg tX: Translatable)
        = containsNotDefaultTranslationOfFunArr(t, tX)

    val containsNot = DescriptionCharSequenceAssertion.CONTAINS_NOT.getDefault()
    val hello = TestTranslatable.HELLO.getDefault()
    val welcome = TestTranslatable.WELCOME.getDefault()

    val indentRootBulletPoint = " ".repeat(rootBulletPoint.length)
    val valueWithIndent = "$indentRootBulletPoint$listBulletPoint$value"

    describeFun(containsDefaultTranslationOf, containsNotDefaultTranslationOf) {

        context("text '$text' and translatables ${TestTranslatable.HELLO} ($hello) and ${TestTranslatable.WELCOME} ($welcome)") {
            test("$containsDefaultTranslationOf ${TestTranslatable.HELLO} does not throw") {
                fluent.containsDefaultTranslationOfFun(TestTranslatable.HELLO)
            }

            test("$containsNotDefaultTranslationOf ${TestTranslatable.HELLO} throws AssertionError") {
                expect {
                    fluent.containsNotDefaultTranslationOfFun(TestTranslatable.HELLO)
                }.toThrow<AssertionError>{}
            }

            test("$containsDefaultTranslationOf ${TestTranslatable.WELCOME} throws AssertionError") {
                expect {
                    fluent.containsDefaultTranslationOfFun(TestTranslatable.WELCOME)
                }.toThrow<AssertionError>{}
            }

            test("$containsNotDefaultTranslationOf ${TestTranslatable.WELCOME} does not throw") {
                fluent.containsNotDefaultTranslationOfFun(TestTranslatable.WELCOME)
            }

            test("$containsDefaultTranslationOf ${TestTranslatable.HELLO} and ${TestTranslatable.WELCOME}, throws AssertionError") {
                expect {
                    fluent.containsDefaultTranslationOfFun(TestTranslatable.HELLO, TestTranslatable.WELCOME)
                }.toThrow<AssertionError> {
                    message {
                        contains("$rootBulletPoint$containsDescr: $separator" +
                            "$valueWithIndent: \"$welcome\"")
                        containsNot("$valueWithIndent: \"$hello\"")
                    }
                }
            }

            test("$containsNotDefaultTranslationOf ${TestTranslatable.HELLO} and ${TestTranslatable.WELCOME}, throws AssertionError") {
                expect {
                    fluent.containsNotDefaultTranslationOfFun(TestTranslatable.HELLO, TestTranslatable.WELCOME)
                }.toThrow<AssertionError> {
                    message {
                        contains("$rootBulletPoint$containsNot: $separator" +
                            "$valueWithIndent: \"$hello\"")
                        containsNot("$valueWithIndent: \"$welcome\"")
                    }
                }
            }
        }
    }


    val isNot = DescriptionBasic.IS_NOT.getDefault()
    val itIs = DescriptionBasic.IS.getDefault()
    describeFun(isEmpty, isNotEmpty) {
        context("string is empty") {
            test("$isEmpty does not throw") {
                assert("").isEmptyFun()
                assert(StringBuilder()).isEmptyFun()
                assert(StringBuffer()).isEmptyFun()
            }
            test("$isNotEmpty throws an AssertionError") {
                expect {
                    assert("").isNotEmptyFun()
                }.toThrow<AssertionError> { message { endsWith("$isNot: empty") } }
                expect {
                    assert(StringBuilder()).isNotEmptyFun()
                }.toThrow<AssertionError> { message { endsWith("$isNot: empty") } }
                expect {
                    assert(StringBuffer()).isNotEmptyFun()
                }.toThrow<AssertionError> { message { endsWith("$isNot: empty") } }
            }
        }
        context("string is not empty") {
            val notEmptyString = "not empty string"
            test("$isEmpty throws an AssertionError") {
                expect {
                    assert(notEmptyString).isEmptyFun()
                }.toThrow<AssertionError> { message { endsWith("$itIs: empty") } }
                expect {
                    assert(StringBuilder(notEmptyString)).isEmptyFun()
                }.toThrow<AssertionError> { message { endsWith("$itIs: empty") } }
                expect {
                    assert(StringBuffer(notEmptyString)).isEmptyFun()
                }.toThrow<AssertionError> { message { endsWith("$itIs: empty") } }
            }
            test("$isNotEmpty does not throw") {
                assert(notEmptyString).isNotEmptyFun()
                assert(StringBuilder(notEmptyString)).isNotEmptyFun()
                assert(StringBuffer(notEmptyString)).isNotEmptyFun()
            }
        }
    }

    describeFun(isNotBlank) {
        context("string is blank") {
            test("$isNotBlank throws an AssertionError") {
                val blankString = "   "
                expect {
                    assert(blankString).isNotBlankFun()
                }.toThrow<AssertionError> { message { endsWith("$isNot: blank") } }
                expect {
                    assert(StringBuilder(blankString)).isNotBlankFun()
                }.toThrow<AssertionError> { message { endsWith("$isNot: blank") } }
                expect {
                    assert(StringBuffer(blankString)).isNotBlankFun()
                }.toThrow<AssertionError> { message { endsWith("$isNot: blank") } }
            }
        }
        context("string is not blank") {
            val notBlankString = "not blank string"
            test("$isNotBlank does not throw") {
                assert(notBlankString).isNotBlankFun()
                assert(StringBuilder(notBlankString)).isNotBlankFun()
                assert(StringBuffer(notBlankString)).isNotBlankFun()
            }
        }
    }

    describeFun(startsWith, startsNotWith) {
        context("text '$text'") {
            test("$startsWith 'Hello' does not throw") {
                fluent.startsWithFun("Hello")
            }
            test("$startsNotWith 'Hello' throws an AssertionError") {
                expect {
                    fluent.startsNotWithFun("Hello")
                }.toThrow<AssertionError> { messageContains(STARTS_NOT_WITH.getDefault()) }
            }

            test("$startsWith 'Robert' throws an AssertionError") {
                expect {
                    fluent.startsWithFun("goodbye")
                }.toThrow<AssertionError> { messageContains(STARTS_WITH.getDefault()) }
            }
            test("$startsNotWith 'Robert' does not throw") {
                fluent.startsNotWithFun("goodbye")
            }
        }
    }

    describeFun(endsWith, endsNotWith) {
        context("text '$text'") {
            test("$endsWith 'Hello' throws an AssertionError") {
                expect {
                    fluent.endsWithFun("Hello")
                }.toThrow<AssertionError> { messageContains(ENDS_WITH.getDefault()) }
            }
            test("$endsNotWith 'Hello' does not throw") {
                fluent.endsNotWithFun("Hello")
            }

            test("$endsWith 'Robert' does not throw") {
                fluent.endsWithFun("Robert")
            }
            test("$endsNotWith 'Robert' throws an AssertionError") {
                expect {
                    fluent.endsNotWithFun("Robert")
                }.toThrow<AssertionError> { messageContains(ENDS_NOT_WITH.getDefault()) }
            }
        }
    }
}) {
    private enum class TestTranslatable(override val value: String) : StringBasedTranslatable {
        HELLO("Hello"),
        WELCOME("Welcome")
    }
}
