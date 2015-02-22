package com.intellij.structuralsearch.impl.matcher.predicates;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.structuralsearch.MatchOptions;
import com.intellij.structuralsearch.MatchResult;
import com.intellij.structuralsearch.Matcher;
import com.intellij.structuralsearch.impl.matcher.MatchContext;
import com.intellij.structuralsearch.impl.matcher.compiler.PatternCompiler;

/**
 * @author Maxim.Mossienko
 */
public class WithinPredicate extends AbstractStringBasedPredicate {
  private final MatchOptions myMatchOptions;
  private Matcher matcher;

  public WithinPredicate(String name, String within, FileType fileType, Project project) {
    super(name, within);
    myMatchOptions = new MatchOptions();

    myMatchOptions.setLooseMatching(true);
    myMatchOptions.setFileType(fileType);
    final String unquoted = StringUtil.stripQuotesAroundValue(within);
    if (!unquoted.equals(within)) {
      myMatchOptions.setSearchPattern(unquoted);
      PatternCompiler.transformOldPattern(myMatchOptions);
      matcher = new Matcher(project, myMatchOptions);
    } else {
      assert false;
    }
  }

  public boolean match(PsiElement node, PsiElement match, int start, int end, MatchContext context) {
    final MatchResult result = matcher.isMatchedByDownUp(match, myMatchOptions);
    if (result == null) {
      return false;
    }
    return PsiTreeUtil.isAncestor(result.getMatch(), match, false);
  }
}